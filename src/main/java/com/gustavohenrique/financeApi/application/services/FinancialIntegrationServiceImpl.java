package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.AccountService;
import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.FinancialIntegrationRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.AccountNotFoundException;
import com.gustavohenrique.financeApi.exception.IntegrationLinkIdNotFoundException;
import com.gustavohenrique.financeApi.exception.IntegrationNotFoundException;
import com.gustavohenrique.financeApi.exception.UserIDNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialIntegrationServiceImpl implements FinancialIntegrationService {
    private final FinancialIntegrationRepository integrationRepository;

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Override
    public FinancialIntegration findById(Long id) {
        return integrationRepository.findById(id)
                .orElseThrow(() -> new IntegrationNotFoundException(id));
    }

    @Override
    public List<FinancialIntegration> findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserIDNotFoundException(userId));
        return integrationRepository.findByUser(user);
    }

    @Override
    public FinancialIntegration create(FinancialIntegration financialIntegration, Account account) {
        if(!userRepository.existsById(financialIntegration.getUser().getId()))
            throw new UserIDNotFoundException(financialIntegration.getUser().getId());

        if(!accountRepository.existsById(account.getId())) throw new AccountNotFoundException(account.getId());

        financialIntegration.setCreatedAt(LocalDateTime.now());
        financialIntegration.setExpiresAt(LocalDateTime.now().plusMonths(12));

        integrationRepository.save(financialIntegration);
        account.setIntegration(financialIntegration);
        accountRepository.save(account);
        return financialIntegration;
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
    public List<Account> listIntegrationAccounts(Long id) {
        FinancialIntegration integration = findById(id);
        return integration.getAccounts();
    }

    @Override
    public FinancialIntegration resolveIntegrationByLinkId(String linkId,Long userId, Long accountId, AggregatorType aggregator) {

        FinancialIntegration integration;
        if(integrationRepository.existsByLinkId(linkId)) return integrationRepository
                .findByLinkId(linkId);
        else integration = new FinancialIntegration();

        integration.setLinkId(linkId);
        integration.setUser(userRepository.findById(userId).orElseThrow(()-> new UserIDNotFoundException(userId)));
        integration.setAggregator(aggregator);
        integration.setCreatedAt(LocalDateTime.now());
        integration.setExpiresAt(LocalDateTime.now().plusMonths(12));

        integrationRepository.save(integration);
        Account account = accountService.findById(accountId);
        account.setIntegration(integration);
        accountRepository.save(account);

        return integration;
    }
}
