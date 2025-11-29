package com.gustavohenrique.financeApi.webhook.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.application.repositories.TransactionRepository;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.webhook.dataTransfer.KafkaMessage;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ListTransactionsResponse;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;
import com.gustavohenrique.financeApi.webhook.service.PluggyResponseMapper;
import com.gustavohenrique.financeApi.webhook.service.RequestService;
import com.gustavohenrique.financeApi.webhook.service.SetUpWebhook;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class WebhookEventConsumer {

    private final ObjectMapper objectMapper;
    private final RequestService pluggyClient;
    private final FinancialIntegrationService financialIntegrationService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final SetUpWebhook setUpWebhook;
    private final PluggyResponseMapper pluggyResponseMapper;

    @Autowired
    public WebhookEventConsumer(ObjectMapper objectMapper, RequestService pluggyClient, FinancialIntegrationService financialIntegrationService, TransactionService transactionService, TransactionRepository transactionRepository, SetUpWebhook setUpWebhook, PluggyResponseMapper pluggyResponseMapper) {
        this.objectMapper = objectMapper;
        this.pluggyClient = pluggyClient;
        this.financialIntegrationService = financialIntegrationService;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.setUpWebhook = setUpWebhook;
        this.pluggyResponseMapper = pluggyResponseMapper;
    }


    @KafkaListener(topics = "webhook-transactions", groupId = "finance-consumer")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            KafkaMessage event = objectMapper.readValue(record.value(), KafkaMessage.class);

            log.info("📥 Mensagem recebida: {}", event);

            User user = new User();
            user.setName("Webhook User");
            user.setEmail("webhookUser@email.com");
            user.setPassword("password");
            Long userWebhookID = setUpWebhook.UserWebhookID(user);

            Account account = new Account(null,"Webhook Account","Open Finance","teste"
                    ,new BigDecimal("1000.00"),user,null,null);
            Account accountWebhook = setUpWebhook.AccountWebhookId(account);

            financialIntegrationService
                    .resolveIntegrationByLinkId(event.getItemId(),userWebhookID ,accountWebhook.getId(), AggregatorType.PLUGGY);

            ListTransactionsResponse transactionsResponse = pluggyClient.fetchTransaction(event.getLinkTransactions());

            for (TransactionResponse result : transactionsResponse.getResults()){
                    Transaction transaction = pluggyResponseMapper.mapPluggyToTransaction(result);
                    transaction.setAccount(accountWebhook);
                    transactionService.create(transaction);
                    log.info("💾 Transação persistida com sucesso");
            }
        } catch (Exception e) {
            log.error("❌ Erro ao processar evento do Kafka", e);
        }
    }
}
