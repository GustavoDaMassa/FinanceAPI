package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.AccountDTO;
import com.gustavohenrique.financeApi.graphql.inputs.AccountInput;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class AccountMapper {

    private final ModelMapper modelMapper;

    public AccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AccountDTO toDto(Account account) {
        AccountDTO dto = modelMapper.map(account, AccountDTO.class);
        dto.setUserId(account.getUser().getId());
        if (account.getIntegration() != null) {
            dto.setIntegrationId(account.getIntegration().getId());
        }
        dto.setBalance(
                Optional.ofNullable(account.getBalance())
                        .orElse(BigDecimal.ZERO)
                        .toPlainString()
        );

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
