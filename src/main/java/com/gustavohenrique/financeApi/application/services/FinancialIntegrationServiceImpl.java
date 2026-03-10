package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.AccountService;
import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.FinancialIntegrationRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.AccountNotFoundException;
import com.gustavohenrique.financeApi.exception.IntegrationLinkIdNotFoundException;
import com.gustavohenrique.financeApi.exception.IntegrationNotFoundException;
import com.gustavohenrique.financeApi.exception.UserNotFoundException;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;
import com.gustavohenrique.financeApi.webhook.service.PluggyResponseMapper;
import com.gustavohenrique.financeApi.webhook.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialIntegrationServiceImpl implements FinancialIntegrationService {
    private final FinancialIntegrationRepository integrationRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final RequestService requestService;
    private final PluggyResponseMapper pluggyResponseMapper;
    private final TransactionService transactionService;

    @Override
    public FinancialIntegration findById(Long id) {
        return integrationRepository.findById(id)
                .orElseThrow(() -> new IntegrationNotFoundException(id));
    }

    @Override
    public List<FinancialIntegration> findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return integrationRepository.findByUser(user);
    }

    @Override
    public FinancialIntegration create(FinancialIntegration financialIntegration) {
        if(!userRepository.existsById(financialIntegration.getUser().getId()))
            throw new UserNotFoundException(financialIntegration.getUser().getId());

        financialIntegration.setCreatedAt(LocalDateTime.now());
        financialIntegration.setExpiresAt(LocalDateTime.now().plusMonths(12));
        financialIntegration.setStatus("UPDATED");

        return integrationRepository.save(financialIntegration);
    }

    @Override
    public FinancialIntegration update(Long id, FinancialIntegration updatedIntegration) {
        FinancialIntegration existing = findById(id);

        existing.setAggregator(updatedIntegration.getAggregator());
        existing.setLinkId(updatedIntegration.getLinkId());
        existing.setStatus(updatedIntegration.getStatus());
        existing.setExpiresAt(updatedIntegration.getExpiresAt());
        existing.setUser(updatedIntegration.getUser());

        return integrationRepository.save(existing);
    }

    @Override
    public FinancialIntegration delete(Long id) {
        FinancialIntegration existing = findById(id);
        integrationRepository.delete(existing);
        return existing;
    }

    @Override
    public FinancialIntegration findByLinkId(String linkId) {
        return integrationRepository.findByLinkId(linkId)
                .orElseThrow(() -> new IntegrationLinkIdNotFoundException(linkId));
    }

    @Override
    public List<Account> listIntegrationAccounts(Long id) {
        FinancialIntegration integration = findById(id);
        return integration.getAccounts();
    }

    @Override
    public FinancialIntegration findByIdForUser(Long integrationId, Long userId) {
        FinancialIntegration integration = findById(integrationId);
        if (!integration.getUser().getId().equals(userId)) {
            throw new SecurityException("Integration does not belong to the authenticated user.");
        }
        return integration;
    }

    @Override
    public FinancialIntegration reconnect(Long integrationId, Long userId) {
        FinancialIntegration integration = findByIdForUser(integrationId, userId);
        integration.setStatus("UPDATED");
        return integrationRepository.save(integration);
    }

    @Override
    public boolean syncTransactions(Long integrationId, Long userId) {
        FinancialIntegration integration = findByIdForUser(integrationId, userId);

        List<Account> linkedAccounts = integration.getAccounts().stream()
                .filter(a -> a.getPluggyAccountId() != null)
                .toList();

        int saved = 0;
        for (Account account : linkedAccounts) {
            List<TransactionResponse> transactions = requestService.fetchTransactionsByAccount(account.getPluggyAccountId());
            for (TransactionResponse tx : transactions) {
                if (transactionService.existsByExternalId(tx.getId())) {
                    continue;
                }
                Transaction transaction = pluggyResponseMapper.mapPluggyToTransaction(tx);
                transaction.setAccount(account);
                transactionService.create(transaction);
                saved++;
            }
        }

        log.info("Sync completed for integration {}. {} transactions imported.", integrationId, saved);
        return true;
    }
}
