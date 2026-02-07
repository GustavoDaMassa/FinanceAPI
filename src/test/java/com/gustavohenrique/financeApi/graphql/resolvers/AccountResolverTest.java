package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.AccountService;
import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.domain.enums.Role;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.AccountDTO;
import com.gustavohenrique.financeApi.graphql.inputs.LinkAccountInput;
import com.gustavohenrique.financeApi.graphql.mappers.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountResolverTest {

    @Mock
    private AccountService accountService;
    @Mock
    private FinancialIntegrationService financialIntegrationService;
    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountResolver accountResolver;

    private User user;
    private Account account;
    private AccountDTO accountDTO;
    private FinancialIntegration integration;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setRole(Role.USER);

        integration = new FinancialIntegration();
        integration.setId(1L);
        integration.setUser(user);

        account = new Account();
        account.setId(1L);
        account.setAccountName("Checking");
        account.setPluggyAccountId("pluggy-123");
        account.setUser(user);
        account.setIntegration(integration);
        account.setBalance(BigDecimal.ZERO);

        accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        accountDTO.setAccountName("Checking");
        accountDTO.setUserId(1L);
    }

    @Test
    @DisplayName("Should list accounts by authenticated user")
    void listAccountsByUser() {
        when(accountService.findByUserId(1L)).thenReturn(List.of(account));
        when(accountMapper.toDto(account)).thenReturn(accountDTO);

        List<AccountDTO> result = accountResolver.listAccountsByUser(user);

        assertEquals(1, result.size());
        assertEquals("Checking", result.get(0).getAccountName());
    }

    @Test
    @DisplayName("Should find account by ID")
    void findAccountById() {
        when(accountService.findById(1L)).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDTO);

        AccountDTO result = accountResolver.findAccountById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Checking", result.getAccountName());
    }

    @Test
    @DisplayName("Should link account from Pluggy")
    void linkAccount_success() {
        LinkAccountInput input = new LinkAccountInput();
        input.setIntegrationId(1L);
        input.setPluggyAccountId("pluggy-123");
        input.setName("Checking");
        input.setType("BANK");

        when(financialIntegrationService.findById(1L)).thenReturn(integration);
        when(accountService.create(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDTO);

        AccountDTO result = accountResolver.linkAccount(input, user);

        assertNotNull(result);
        assertEquals("Checking", result.getAccountName());
        verify(accountService).create(any(Account.class));
    }

    @Test
    @DisplayName("Should throw when linking account from another user's integration")
    void linkAccount_wrongUser() {
        User otherUser = new User();
        otherUser.setId(2L);

        FinancialIntegration otherIntegration = new FinancialIntegration();
        otherIntegration.setId(1L);
        otherIntegration.setUser(otherUser);

        LinkAccountInput input = new LinkAccountInput();
        input.setIntegrationId(1L);

        when(financialIntegrationService.findById(1L)).thenReturn(otherIntegration);

        assertThrows(SecurityException.class, () -> accountResolver.linkAccount(input, user));
        verify(accountService, never()).create(any());
    }

    @Test
    @DisplayName("Should delete account")
    void deleteAccount() {
        when(accountService.delete(1L)).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDTO);

        AccountDTO result = accountResolver.deleteAccount(1L);

        assertEquals(1L, result.getId());
        verify(accountService).delete(1L);
    }
}
