package com.gustavohenrique.financeApi.webhook.consumer;

import com.gustavohenrique.financeApi.webhook.dataTransfer.ClientCredencials;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ListTransactionsResponse;
import com.gustavohenrique.financeApi.webhook.producer.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Component
public class RequestService {

    private final PluggyAuthClient authClient;
    private final CredentialService credentialService;

    @Autowired
    public RequestService(PluggyAuthClient authClient, CredentialService credentialService){
        this.authClient = authClient;
        this.credentialService = credentialService;
    }

    public ListTransactionsResponse fetchTransaction(String linkTransactions) throws IOException {
        ClientCredencials clientCredencials = credentialService.readCredentials();
        String token = authClient.getAccessToken(clientCredencials.getClientId(), clientCredencials.getClientSecret());
        WebClient client = WebClient.builder()
                .defaultHeader("X-API-KEY", token)
                .defaultHeader("accept", "application/json")
                .build();
        return client.get()
                .uri(linkTransactions)
                .retrieve()
                .bodyToMono(ListTransactionsResponse.class)
                .block();
    }
}
