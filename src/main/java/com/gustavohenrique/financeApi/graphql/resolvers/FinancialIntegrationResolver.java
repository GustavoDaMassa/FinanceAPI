package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.application.repositories.FinancialIntegrationRepository;
import com.gustavohenrique.financeApi.application.repositories.TransactionRepository;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.AccountDTO;
import com.gustavohenrique.financeApi.graphql.dtos.ConnectTokenDTO;
import com.gustavohenrique.financeApi.graphql.dtos.FinancialIntegrationDTO;
import com.gustavohenrique.financeApi.graphql.dtos.PluggyAccountDTO;
import com.gustavohenrique.financeApi.graphql.inputs.FinancialIntegrationInput;
import com.gustavohenrique.financeApi.graphql.mappers.AccountMapper;
import com.gustavohenrique.financeApi.graphql.mappers.FinancialIntegrationMapper;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;
import com.gustavohenrique.financeApi.webhook.service.PluggyResponseMapper;
import com.gustavohenrique.financeApi.webhook.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FinancialIntegrationResolver {

    private final FinancialIntegrationService integrationService;
    private final FinancialIntegrationRepository integrationRepository;
    private final RequestService requestService;
    private final FinancialIntegrationMapper mapper;
    private final AccountMapper accountMapper;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final PluggyResponseMapper pluggyResponseMapper;



    @QueryMapping
    public FinancialIntegrationDTO findFinancialIntegrationById(@Argument Long id) {
        FinancialIntegration integration = integrationService.findById(id);
        return mapper.toDto(integration);
    }

    @QueryMapping
    public List<FinancialIntegrationDTO> listFinancialIntegrationsByUser(@AuthenticationPrincipal User user) {
        return integrationService.findByUserId(user.getId())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<AccountDTO> listAccountsByIntegration(@Argument Long id){
        return integrationService.listIntegrationAccounts(id).stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public ConnectTokenDTO createConnectToken(@AuthenticationPrincipal User user) {
        String token = requestService.createConnectToken();
        return new ConnectTokenDTO(token);
    }

    @QueryMapping
    public ConnectTokenDTO createConnectTokenForItem(@Argument String itemId, @AuthenticationPrincipal User user) {
        String token = requestService.createConnectToken(itemId);
        return new ConnectTokenDTO(token);
    }

    @QueryMapping
    public List<PluggyAccountDTO> accountsFromPluggy(@Argument Long integrationId, @AuthenticationPrincipal User user) {
        FinancialIntegration integration = integrationService.findById(integrationId);
        if (!integration.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Integration does not belong to the authenticated user.");
        }
        return requestService.fetchAccounts(integration.getLinkId());
    }



    @MutationMapping
    public FinancialIntegrationDTO createFinancialIntegration(@Argument String itemId, @AuthenticationPrincipal User user) {
        FinancialIntegration newIntegration = new FinancialIntegration();
        newIntegration.setLinkId(itemId);
        newIntegration.setAggregator(com.gustavohenrique.financeApi.domain.enums.AggregatorType.PLUGGY);
        newIntegration.setStatus("UPDATED");
        newIntegration.setUser(user);
        FinancialIntegration created = integrationService.create(newIntegration);
        return mapper.toDto(created);
    }

    @MutationMapping
    public FinancialIntegrationDTO updateFinancialIntegration(@Argument Long id, @Argument("financialIntegration") FinancialIntegrationInput input) {
        // This might need adjustment if the input and mapper are user-dependent
        FinancialIntegration existingIntegration = integrationService.findById(id);
        User user = existingIntegration.getUser();
        FinancialIntegration updated = integrationService.update(id, mapper.fromInput(input, user));
        return mapper.toDto(updated);
    }

    @MutationMapping
    public FinancialIntegrationDTO deleteFinancialIntegration(@Argument Long id) {
        FinancialIntegration deleted = integrationService.delete(id);
        return mapper.toDto(deleted);
    }

    @MutationMapping
    public FinancialIntegrationDTO reconnectIntegration(@Argument Long integrationId, @AuthenticationPrincipal User user) {
        FinancialIntegration integration = integrationService.findById(integrationId);
        if (!integration.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Integration does not belong to the authenticated user.");
        }
        integration.setStatus("UPDATED");
        FinancialIntegration saved = integrationRepository.save(integration);
        return mapper.toDto(saved);
    }

    @MutationMapping
    public boolean syncIntegrationTransactions(@Argument Long integrationId, @AuthenticationPrincipal User user) {
        FinancialIntegration integration = integrationService.findById(integrationId);
        if (!integration.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Integration does not belong to the authenticated user.");
        }

        List<Account> linkedAccounts = integration.getAccounts().stream()
                .filter(a -> a.getPluggyAccountId() != null)
                .toList();

        int saved = 0;
        for (Account account : linkedAccounts) {
            List<TransactionResponse> transactions = requestService.fetchTransactionsByAccount(account.getPluggyAccountId());
            for (TransactionResponse tx : transactions) {
                if (transactionRepository.existsByExternalId(tx.getId())) {
                    continue;
                }
                Transaction transaction = pluggyResponseMapper.mapPluggyToTransaction(tx);
                transaction.setAccount(account);
                transactionService.create(transaction);
                saved++;
            }
        }

        log.info("✅ Sync completed for integration {}. {} transactions imported.", integrationId, saved);
        return true;
    }
}
