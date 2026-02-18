package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.AccountDTO;
import com.gustavohenrique.financeApi.graphql.inputs.AccountInput;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class AccountMapper {

    public AccountDTO toDto(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountName(account.getAccountName());
        dto.setInstitution(account.getInstitution());
        dto.setType(account.getType());
        dto.setUserId(account.getUser().getId());
        dto.setBalance(
                Optional.ofNullable(account.getBalance())
                        .orElse(BigDecimal.ZERO)
                        .toPlainString()
        );
        if (account.getIntegration() != null) {
            dto.setIntegrationId(account.getIntegration().getId());
        }
        return dto;
    }

    public Account fromInput(AccountInput input, User user, FinancialIntegration integration) {
        Account account = new Account();
        account.setAccountName(input.getAccountName());
        account.setInstitution(input.getInstitution());
        account.setType(input.getType());
        account.setUser(user);
        account.setIntegration(integration);
        return account;
    }
}
