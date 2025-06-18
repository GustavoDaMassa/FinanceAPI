package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.AccountService;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.AccountDTO;
import com.gustavohenrique.financeApi.graphql.inputs.AccountInput;
import com.gustavohenrique.financeApi.graphql.mappers.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AccountResolver {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    // ===== Queries =====

    @QueryMapping
    public AccountDTO findAccountById(@Argument Long id) {
        Account account = accountService.findById(id);
        return accountMapper.toDto(account);
    }

    @QueryMapping
    public List<AccountDTO> listAccountsByUser(@Argument Long userId) {
        return accountService.findByUserId(userId)
                .stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }

    // ===== Mutations =====

    @MutationMapping
    public AccountDTO createAccount(@Argument AccountInput input) {
        User user = accountService.findUserById(input.getUserId());
        FinancialIntegration integration = input.getIntegrationId() != null
                ? accountService.findIntegrationById(input.getIntegrationId())
                : null;

        Account created = accountService.create(accountMapper.fromInput(input, user, integration));
        return accountMapper.toDto(created);
    }

    @MutationMapping
    public AccountDTO updateAccount(@Argument Long id, @Argument AccountInput input) {
        User user = accountService.findUserById(input.getUserId());
        FinancialIntegration integration = input.getIntegrationId() != null
                ? accountService.findIntegrationById(input.getIntegrationId())
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
