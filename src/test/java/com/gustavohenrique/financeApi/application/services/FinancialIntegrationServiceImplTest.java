package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.FinancialIntegrationRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinancialIntegrationServiceImplTest {

    @Mock
    private FinancialIntegrationRepository integrationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FinancialIntegrationServiceImpl integrationService;

    @Mock
    private AccountRepository accountRepository;

    private FinancialIntegration integration;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Gustavo", "gustavo@test.com", "123456", null, null);
        integration = new FinancialIntegration(1L, AggregatorType.PLUGGY, "link123", "active", null, null, user, null);
    }

    @Test
    @DisplayName("Should return integration when ID exists")
    void findById_success() {
        when(integrationRepository.findById(1L)).thenReturn(Optional.of(integration));

        FinancialIntegration result = integrationService.findById(1L);

        assertNotNull(result);
        assertEquals("link123", result.getLinkId());
    }

    @Test
    @DisplayName("Should throw when integration ID is not found")
    void findById_notFound_shouldThrow() {
        when(integrationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IntegrationNotFoundException.class, () -> integrationService.findById(1L));
    }

    @Test
    @DisplayName("Should return list of integrations for user ID")
    void findByUserId_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(integrationRepository.findByUser(user)).thenReturn(List.of(integration));

        List<FinancialIntegration> result = integrationService.findByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("link123", result.getFirst().getLinkId());
    }

    @Test
    @DisplayName("Should throw when user ID is not found")
    void findByUserId_notFound_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserIDNotFoundException.class, () -> integrationService.findByUserId(1L));
    }

    @Test
    @DisplayName("Should create integration with timestamps and return it")
     void create_success() {
        // Arrange
        Account account = new Account();
        account.setId(10L);
        account.setUser(user);

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(accountRepository.existsById(account.getId())).thenReturn(true);
        when(integrationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(accountRepository.save(any())).thenReturn(account);

        // Act
        FinancialIntegration result = integrationService.create(integration, account);

        // Assert
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getExpiresAt());
        assertTrue(result.getExpiresAt().isAfter(result.getCreatedAt()));

        verify(integrationRepository).save(any());
        verify(accountRepository).save(any());
    }


    @Test
    @DisplayName("Should throw when updating non-existing integration")
    void update_notFound_shouldThrow() {
        when(integrationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IntegrationNotFoundException.class, () -> integrationService.update(1L, integration));
    }

    @Test
    @DisplayName("Should delete and return integration when ID exists")
    void delete_success() {
        when(integrationRepository.findById(1L)).thenReturn(Optional.of(integration));

        FinancialIntegration result = integrationService.delete(1L);

        assertNotNull(result);
        verify(integrationRepository).delete(integration);
    }

    @Test
    @DisplayName("Should throw when deleting non-existing integration")
    void delete_notFound_shouldThrow() {
        when(integrationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IntegrationNotFoundException.class, () -> integrationService.delete(1L));
    }
}
