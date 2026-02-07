package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import com.gustavohenrique.financeApi.domain.enums.Role;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.FinancialIntegrationDTO;
import com.gustavohenrique.financeApi.graphql.dtos.PluggyAccountDTO;
import com.gustavohenrique.financeApi.graphql.mappers.AccountMapper;
import com.gustavohenrique.financeApi.graphql.mappers.FinancialIntegrationMapper;
import com.gustavohenrique.financeApi.webhook.service.RequestService;
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
class FinancialIntegrationResolverTest {

    @Mock
    private FinancialIntegrationService integrationService;
    @Mock
    private RequestService requestService;
    @Mock
    private FinancialIntegrationMapper mapper;
    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private FinancialIntegrationResolver resolver;

    private User user;
    private FinancialIntegration integration;
    private FinancialIntegrationDTO integrationDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setRole(Role.USER);

        integration = new FinancialIntegration();
        integration.setId(1L);
        integration.setLinkId("item-123");
        integration.setAggregator(AggregatorType.PLUGGY);
        integration.setUser(user);

        integrationDTO = new FinancialIntegrationDTO();
        integrationDTO.setId(1L);
        integrationDTO.setLinkId("item-123");
        integrationDTO.setUserId(1L);
    }

    @Test
    @DisplayName("Should find integration by ID")
    void findFinancialIntegrationById() {
        when(integrationService.findById(1L)).thenReturn(integration);
        when(mapper.toDto(integration)).thenReturn(integrationDTO);

        FinancialIntegrationDTO result = resolver.findFinancialIntegrationById(1L);

        assertEquals(1L, result.getId());
        assertEquals("item-123", result.getLinkId());
    }

    @Test
    @DisplayName("Should list integrations for authenticated user")
    void listFinancialIntegrationsByUser() {
        when(integrationService.findByUserId(1L)).thenReturn(List.of(integration));
        when(mapper.toDto(integration)).thenReturn(integrationDTO);

        List<FinancialIntegrationDTO> result = resolver.listFinancialIntegrationsByUser(user);

        assertEquals(1, result.size());
        assertEquals("item-123", result.get(0).getLinkId());
    }

    @Test
    @DisplayName("Should create integration for authenticated user")
    void createFinancialIntegration() {
        when(integrationService.create(any(FinancialIntegration.class))).thenReturn(integration);
        when(mapper.toDto(integration)).thenReturn(integrationDTO);

        FinancialIntegrationDTO result = resolver.createFinancialIntegration("item-123", user);

        assertNotNull(result);
        assertEquals("item-123", result.getLinkId());
        verify(integrationService).create(any(FinancialIntegration.class));
    }

    @Test
    @DisplayName("Should fetch accounts from Pluggy for user's integration")
    void accountsFromPluggy_success() {
        PluggyAccountDTO pluggyAccount = new PluggyAccountDTO("acc-1", "Checking", "BANK", new BigDecimal("1000"), "BRL");

        when(integrationService.findById(1L)).thenReturn(integration);
        when(requestService.fetchAccounts("item-123")).thenReturn(List.of(pluggyAccount));

        List<PluggyAccountDTO> result = resolver.accountsFromPluggy(1L, user);

        assertEquals(1, result.size());
        assertEquals("Checking", result.get(0).getName());
    }

    @Test
    @DisplayName("Should throw when fetching accounts from another user's integration")
    void accountsFromPluggy_wrongUser() {
        User otherUser = new User();
        otherUser.setId(2L);

        FinancialIntegration otherIntegration = new FinancialIntegration();
        otherIntegration.setId(1L);
        otherIntegration.setUser(otherUser);

        when(integrationService.findById(1L)).thenReturn(otherIntegration);

        assertThrows(SecurityException.class, () -> resolver.accountsFromPluggy(1L, user));
        verify(requestService, never()).fetchAccounts(any());
    }

    @Test
    @DisplayName("Should delete integration")
    void deleteFinancialIntegration() {
        when(integrationService.delete(1L)).thenReturn(integration);
        when(mapper.toDto(integration)).thenReturn(integrationDTO);

        FinancialIntegrationDTO result = resolver.deleteFinancialIntegration(1L);

        assertEquals(1L, result.getId());
        verify(integrationService).delete(1L);
    }
}
