package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.AccountService;
import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.AccountDTO;
import com.gustavohenrique.financeApi.graphql.inputs.AccountInput;
import com.gustavohenrique.financeApi.graphql.inputs.LinkAccountInput;
import com.gustavohenrique.financeApi.graphql.mappers.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class AccountResolver {

    private final AccountService accountService;
    private final FinancialIntegrationService financialIntegrationService;
    private final AccountMapper accountMapper;


    @QueryMapping
    public AccountDTO findAccountById(@Argument Long id) {
        Account account = accountService.findById(id);
        return accountMapper.toDto(account);
    }

    @QueryMapping
    public List<AccountDTO> listAccountsByUser(@AuthenticationPrincipal User user) {
        return accountService.findByUserId(user.getId())
                .stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }


    @MutationMapping
    public AccountDTO createAccount(@Argument AccountInput input, @AuthenticationPrincipal User user) {
        FinancialIntegration integration = input.getIntegrationId() != null
                ? financialIntegrationService.findById(input.getIntegrationId())
                : null;

        Account created = accountService.create(accountMapper.fromInput(input, user, integration));
        return accountMapper.toDto(created);
    }
    
    @MutationMapping
    public AccountDTO linkAccount(@Argument LinkAccountInput input, @AuthenticationPrincipal User user) {
        FinancialIntegration integration = financialIntegrationService.findById(input.getIntegrationId());
        if (!integration.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Integration does not belong to the authenticated user.");
        }

        Account newAccount = new Account();
        newAccount.setPluggyAccountId(input.getPluggyAccountId());
        newAccount.setAccountName(input.getName());
        newAccount.setType(input.getType());
        newAccount.setUser(user);
        newAccount.setIntegration(integration);
        
        Account created = accountService.create(newAccount);
        return accountMapper.toDto(created);
    }

    @MutationMapping
    public AccountDTO updateAccount(@Argument Long id, @Argument AccountInput input, @AuthenticationPrincipal User user) {
        FinancialIntegration integration = input.getIntegrationId() != null
                ? financialIntegrationService.findById(input.getIntegrationId())
                : null;

        Account updated = accountService.update(id, accountMapper.fromInput(input, user, integration));
        return accountMapper.toDto(updated);
    }

    @MutationMapping
    public AccountDTO deleteAccount(@Argument Long id) {
        Account deleted = accountService.delete(id);
        return accountMapper.toDto(deleted);
    }
}
