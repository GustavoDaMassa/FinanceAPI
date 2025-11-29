package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.FinancialIntegrationRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.AccountNotFoundException;
import com.gustavohenrique.financeApi.exception.IntegrationNotFoundException;
import com.gustavohenrique.financeApi.exception.UserIDNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FinancialIntegrationRepository integrationRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User user;
    private Account account;
    private FinancialIntegration integration;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Gustavo");
        user.setEmail("gustavo@test.com");
        user.setPassword("123456");

        integration = new FinancialIntegration();
        integration.setId(10L);
        account = new Account(1L, "Main Account", "Bank", "CHECKING", BigDecimal.valueOf(1000), user, integration, null);
    }

    @Test
    @DisplayName("Should return account when ID exists")
    void findById_success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.findById(1L);

        assertNotNull(result);
        assertEquals("Main Account", result.getAccountName());
    }

    @Test
    @DisplayName("Should throw when account ID not found")
    void findById_notFound_shouldThrow() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.findById(1L));
    }

    @Test
    @DisplayName("Should return accounts for user ID")
    void findByUserId_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountRepository.findByUser(user)).thenReturn(List.of(account));

        List<Account> accounts = accountService.findByUserId(1L);

        assertEquals(1, accounts.size());
        assertEquals("Main Account", accounts.get(0).getAccountName());
    }

    @Test
    @DisplayName("Should throw when user ID not found")
    void findByUserId_notFound_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserIDNotFoundException.class, () -> accountService.findByUserId(1L));
    }

    @Test
    @DisplayName("Should return integration when ID exists")
    void findIntegrationById_success() {
        when(integrationRepository.findById(10L)).thenReturn(Optional.of(integration));

        FinancialIntegration result = accountService.findIntegrationById(10L);

        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    @DisplayName("Should throw when integration ID not found")
    void findIntegrationById_notFound_shouldThrow() {
        when(integrationRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(IntegrationNotFoundException.class, () -> accountService.findIntegrationById(10L));
    }

    @Test
    @DisplayName("Should create and return account")
    void create_success() {
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.create(account);

        assertNotNull(result);
        assertEquals("Main Account", result.getAccountName());
        verify(accountRepository).save(account);
    }

    @Test
    @DisplayName("Should update and return account when ID exists")
    void update_success() {
        Account updated = new Account();
        updated.setAccountName("Updated Account");
        updated.setInstitution("New Bank");
        updated.setType("SAVINGS");
        updated.setBalance(BigDecimal.valueOf(2000));
        updated.setUser(user);
        updated.setIntegration(integration);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(updated);

        Account result = accountService.update(1L, updated);

        assertEquals("Updated Account", result.getAccountName());
        assertEquals("New Bank", result.getInstitution());
        assertEquals("SAVINGS", result.getType());
        assertEquals(BigDecimal.valueOf(2000), result.getBalance());
    }

    @Test
    @DisplayName("Should throw when updating account with non-existing ID")
    void update_notFound_shouldThrow() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Account updated = new Account();
        assertThrows(AccountNotFoundException.class, () -> accountService.update(1L, updated));
    }

    @Test
    @DisplayName("Should delete and return account when ID exists")
    void delete_success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account deleted = accountService.delete(1L);

        assertNotNull(deleted);
        verify(accountRepository).delete(account);
    }

    @Test
    @DisplayName("Should throw when deleting non-existing account")
    void delete_notFound_shouldThrow() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.delete(1L));
    }
}
