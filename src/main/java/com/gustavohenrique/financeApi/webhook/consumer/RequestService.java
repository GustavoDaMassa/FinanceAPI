package com.gustavohenrique.financeApi.webhook.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ListTransactionsResponse;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class PluggyClient {

    private final PluggyAuthClient authClient;
    private final AccountRepository accountRepository;
    private final PluggyResponseMapper responseMapper;
    private final PluggyConfig config;

    @Autowired
    public PluggyClient(PluggyAuthClient authClient,AccountRepository accountRepository,PluggyResponseMapper responseMapper
    ,PluggyConfig config){
        this.authClient = authClient;
        this.accountRepository = accountRepository;
        this.responseMapper = responseMapper;
        this.config = config;
    }

    public ListTransactionsResponse fetchTransaction(String transactionId, Long accountId, String linkTransactions) {
        String token = authClient.getAccessToken(config.getClientId(), config.getClientSecret());
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
