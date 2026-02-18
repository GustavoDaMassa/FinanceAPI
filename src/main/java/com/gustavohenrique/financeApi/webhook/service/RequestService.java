package com.gustavohenrique.financeApi.webhook.service;

import com.gustavohenrique.financeApi.graphql.dtos.PluggyAccountDTO;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ClientCredencials;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ConnectTokenResponse;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ListAccountsResponse;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ListTransactionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Component
public class RequestService {

    private final WebClient webClient;
    private final CredentialService credentialService;
    private final PluggyAuthClient authClient;

    @Value("${pluggy.base-url:https://api.pluggy.ai}")
    private String pluggyBaseUrl;

    @Autowired
    public RequestService(CredentialService credentialService, PluggyAuthClient authClient) {
        this.credentialService = credentialService;
        this.authClient = authClient;
        this.webClient = WebClient.builder().baseUrl(pluggyBaseUrl).build();
    }

    public ListTransactionsResponse fetchTransaction(String linkTransactions) {
        ClientCredencials clientCredencials = credentialService.readCredentials();
        String token = authClient.getAccessToken(clientCredencials.getClientId(), clientCredencials.getClientSecret());
        return webClient.get()
                .uri(linkTransactions)
                .header("X-API-KEY", token)
                .retrieve()
                .bodyToMono(ListTransactionsResponse.class)
                .block();
    }

    public String createConnectToken() {
        ClientCredencials clientCredencials = credentialService.readCredentials();
        String token = authClient.getAccessToken(clientCredencials.getClientId(), clientCredencials.getClientSecret());

        ConnectTokenResponse response = webClient.post()
                .uri("/connect_token")
                .header("X-API-KEY", token)
                .bodyValue("{}")
                .retrieve()
                .bodyToMono(ConnectTokenResponse.class)
                .block();

        return response != null ? response.accessToken() : null;
    }

    public List<PluggyAccountDTO> fetchAccounts(String itemId) {
        ClientCredencials clientCredencials = credentialService.readCredentials();
        String token = authClient.getAccessToken(clientCredencials.getClientId(), clientCredencials.getClientSecret());

        ListAccountsResponse response = webClient.get()
                .uri("/accounts?itemId=" + itemId)
                .header("X-API-KEY", token)
                .retrieve()
                .bodyToMono(ListAccountsResponse.class)
                .block();
        
        return response != null ? response.getResults() : List.of();
    }
}
