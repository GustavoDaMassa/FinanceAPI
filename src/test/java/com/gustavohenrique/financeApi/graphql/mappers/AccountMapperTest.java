package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.AccountDTO;
import com.gustavohenrique.financeApi.graphql.inputs.AccountInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountMapperTest {

    private AccountMapper accountMapper;

    @BeforeEach
    void setUp() {
        accountMapper = new AccountMapper();
    }

    @Test
    @DisplayName("Should map Account to AccountDTO")
    void toDto() {
        User user = new User();
        user.setId(1L);

        FinancialIntegration integration = new FinancialIntegration();
        integration.setId(2L);

        Account account = new Account();
        account.setId(10L);
        account.setAccountName("Checking");
        account.setInstitution("Bank");
        account.setType("checking");
        account.setBalance(new BigDecimal("1500.75"));
        account.setUser(user);
        account.setIntegration(integration);

        AccountDTO dto = accountMapper.toDto(account);

        assertEquals(10L, dto.getId());
        assertEquals("Checking", dto.getAccountName());
        assertEquals(1L, dto.getUserId());
        assertEquals(2L, dto.getIntegrationId());
        assertEquals("1500.75", dto.getBalance());
    }

    @Test
    @DisplayName("Should map Account to DTO with null integration")
    void toDto_nullIntegration() {
        User user = new User();
        user.setId(1L);

        Account account = new Account();
        account.setId(10L);
        account.setAccountName("Savings");
        account.setBalance(null);
        account.setUser(user);
        account.setIntegration(null);

        AccountDTO dto = accountMapper.toDto(account);

        assertNull(dto.getIntegrationId());
        assertEquals("0", dto.getBalance());
    }

    @Test
    @DisplayName("Should map AccountInput to Account")
    void fromInput() {
        AccountInput input = new AccountInput();
        input.setAccountName("Checking");
        input.setInstitution("Bank");
        input.setType("checking");

        User user = new User();
        user.setId(1L);

        FinancialIntegration integration = new FinancialIntegration();
        integration.setId(2L);

        Account result = accountMapper.fromInput(input, user, integration);

        assertEquals("Checking", result.getAccountName());
        assertEquals("Bank", result.getInstitution());
        assertEquals(user, result.getUser());
        assertEquals(integration, result.getIntegration());
    }
}
