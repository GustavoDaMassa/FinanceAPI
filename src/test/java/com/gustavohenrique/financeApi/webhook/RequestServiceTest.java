package com.gustavohenrique.financeApi.webhook;

import com.gustavohenrique.financeApi.graphql.dtos.PluggyAccountDTO;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ClientCredencials;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ListAccountsResponse;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ListTransactionsResponse;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;
import com.gustavohenrique.financeApi.webhook.service.CredentialService;
import com.gustavohenrique.financeApi.webhook.service.PluggyAuthClient;
import com.gustavohenrique.financeApi.webhook.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestServiceTest {

    @Mock
    private CredentialService credentialService;
    @Mock
    private PluggyAuthClient authClient;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    private RequestService requestService;

    @BeforeEach
    void setUp() {
        requestService = new RequestService(credentialService, authClient);
        ReflectionTestUtils.setField(requestService, "webClient", webClient);
    }

    @Test
    @DisplayName("Should fetch accounts from Pluggy API")
    void fetchAccounts_success() {
        ClientCredencials creds = new ClientCredencials();
        creds.setClientId("client-id");
        creds.setClientSecret("client-secret");

        PluggyAccountDTO account1 = new PluggyAccountDTO("acc-1", "Checking", "BANK", new BigDecimal("1500.50"), "BRL");
        PluggyAccountDTO account2 = new PluggyAccountDTO("acc-2", "Savings", "BANK", new BigDecimal("5000.00"), "BRL");

        ListAccountsResponse response = new ListAccountsResponse();
        response.setResults(List.of(account1, account2));

        when(credentialService.readCredentials()).thenReturn(creds);
        when(authClient.getAccessToken("client-id", "client-secret")).thenReturn("test-token");
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ListAccountsResponse.class)).thenReturn(Mono.just(response));

        List<PluggyAccountDTO> accounts = requestService.fetchAccounts("item-123");

        assertEquals(2, accounts.size());
        assertEquals("acc-1", accounts.get(0).getId());
        assertEquals("Checking", accounts.get(0).getName());
    }

    @Test
    @DisplayName("Should return empty list when response has no results")
    void fetchAccounts_emptyResults() {
        ClientCredencials creds = new ClientCredencials();
        creds.setClientId("client-id");
        creds.setClientSecret("client-secret");

        ListAccountsResponse response = new ListAccountsResponse();
        response.setResults(List.of());

        when(credentialService.readCredentials()).thenReturn(creds);
        when(authClient.getAccessToken("client-id", "client-secret")).thenReturn("test-token");
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ListAccountsResponse.class)).thenReturn(Mono.just(response));

        List<PluggyAccountDTO> accounts = requestService.fetchAccounts("item-123");

        assertTrue(accounts.isEmpty());
    }

    @Test
    @DisplayName("Should fetch transactions from Pluggy API")
    void fetchTransaction_success() {
        ClientCredencials creds = new ClientCredencials();
        creds.setClientId("client-id");
        creds.setClientSecret("client-secret");

        TransactionResponse tx = new TransactionResponse();
        tx.setId("tx-1");
        tx.setAmount(new BigDecimal("100.00"));
        tx.setType("CREDIT");

        ListTransactionsResponse response = new ListTransactionsResponse();
        response.setResults(List.of(tx));

        when(credentialService.readCredentials()).thenReturn(creds);
        when(authClient.getAccessToken("client-id", "client-secret")).thenReturn("test-token");
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(ListTransactionsResponse.class)).thenReturn(Mono.just(response));

        ListTransactionsResponse result = requestService.fetchTransaction("http://api.pluggy.ai/transactions");

        assertEquals(1, result.getResults().size());
        assertEquals("tx-1", result.getResults().get(0).getId());
    }
}
