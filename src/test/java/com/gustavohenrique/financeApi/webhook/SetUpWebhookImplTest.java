package com.gustavohenrique.financeApi.webhook;

import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.enums.Role;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.webhook.service.SetUpWebhookImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SetUpWebhookImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private SetUpWebhookImpl setUpWebhook;

    @Test
    @DisplayName("Should return existing user ID when user exists")
    void userWebhookID_existing() {
        User user = new User(1L, null, "test@test.com", null, Role.USER, null, null);

        User existingUser = new User(5L, null, "test@test.com", null, Role.USER, null, null);

        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(existingUser));

        Long result = setUpWebhook.UserWebhookID(user);

        assertEquals(5L, result);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create and return new user ID when user does not exist")
    void userWebhookID_new() {
        User user = new User();
        user.setEmail("new@test.com");
        user.setRole(Role.USER);

        User savedUser = new User(10L, null, "new@test.com", null, Role.USER, null, null);

        when(userRepository.existsByEmail("new@test.com")).thenReturn(false);
        when(userRepository.save(user)).thenReturn(savedUser);

        Long result = setUpWebhook.UserWebhookID(user);

        assertEquals(10L, result);
    }

    @Test
    @DisplayName("Should return existing account when account exists")
    void accountWebhookId_existing() {
        Account account = new Account();
        account.setAccountName("My Account");

        Account existingAccount = new Account(3L, "My Account", null, null, null, null, null, null, null);

        when(accountRepository.existsByAccountName("My Account")).thenReturn(true);
        when(accountRepository.findByAccountName("My Account")).thenReturn(existingAccount);

        Account result = setUpWebhook.AccountWebhookId(account);

        assertEquals(3L, result.getId());
        verify(accountRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should create and return new account when account does not exist")
    void accountWebhookId_new() {
        Account account = new Account();
        account.setAccountName("New Account");

        Account savedAccount = new Account(7L, "New Account", null, null, null, null, null, null, null);

        when(accountRepository.existsByAccountName("New Account")).thenReturn(false);
        when(accountRepository.save(account)).thenReturn(savedAccount);

        Account result = setUpWebhook.AccountWebhookId(account);

        assertEquals(7L, result.getId());
    }
}
