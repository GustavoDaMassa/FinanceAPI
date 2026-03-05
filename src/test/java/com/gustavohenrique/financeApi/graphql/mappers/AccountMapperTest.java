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
        User user = new User(1L, null, null, null, null, null, null);

        FinancialIntegration integration = new FinancialIntegration(2L, null, null, null, null, null, null, null);

        Account account = new Account(10L, "Checking", "Bank", "checking", new BigDecimal("1500.75"), null, user, integration, null);

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
        User user = new User(1L, null, null, null, null, null, null);

        Account account = new Account(10L, "Savings", null, null, null, null, user, null, null);

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
        input.setDescription("checking");

        User user = new User(1L, null, null, null, null, null, null);

        FinancialIntegration integration = new FinancialIntegration(2L, null, null, null, null, null, null, null);

        Account result = accountMapper.fromInput(input, user, integration);

        assertEquals("Checking", result.getAccountName());
        assertEquals("Bank", result.getInstitution());
        assertEquals(user, result.getUser());
        assertEquals(integration, result.getIntegration());
    }
}
