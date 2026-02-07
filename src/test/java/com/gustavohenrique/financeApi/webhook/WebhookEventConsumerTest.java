package com.gustavohenrique.financeApi.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.domain.enums.Role;
import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.IntegrationLinkIdNotFoundException;
import com.gustavohenrique.financeApi.webhook.consumer.WebhookEventConsumer;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ListTransactionsResponse;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;
import com.gustavohenrique.financeApi.webhook.service.PluggyResponseMapper;
import com.gustavohenrique.financeApi.webhook.service.RequestService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebhookEventConsumerTest {

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private RequestService pluggyClient;
    @Mock
    private FinancialIntegrationService financialIntegrationService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PluggyResponseMapper pluggyResponseMapper;

    @InjectMocks
    private WebhookEventConsumer webhookEventConsumer;

    private User user;
    private FinancialIntegration integration;
    private Account account;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        user.setRole(Role.USER);

        integration = new FinancialIntegration();
        integration.setId(1L);
        integration.setLinkId("item-123");
        integration.setUser(user);

        account = new Account();
        account.setId(1L);
        account.setPluggyAccountId("pluggy-acc-123");
        account.setUser(user);
    }

    @Test
    @DisplayName("Should process webhook and create transaction for linked account")
    void consume_success() throws Exception {
        String kafkaPayload = "{\"itemId\":\"item-123\",\"linkTransactions\":\"http://api.pluggy.ai/transactions\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("webhook-transactions", 0, 0, null, kafkaPayload);

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setAccountId("pluggy-acc-123");
        transactionResponse.setAmount(new BigDecimal("100.00"));
        transactionResponse.setType("CREDIT");
        transactionResponse.setDescription("Salary");
        transactionResponse.setDate(ZonedDateTime.now());

        ListTransactionsResponse listResponse = new ListTransactionsResponse();
        listResponse.setResults(List.of(transactionResponse));

        Transaction mappedTransaction = new Transaction();
        mappedTransaction.setAmount(new BigDecimal("100.00"));
        mappedTransaction.setType(TransactionType.INFLOW);

        when(objectMapper.readValue(eq(kafkaPayload), any(Class.class))).thenReturn(
                new com.gustavohenrique.financeApi.webhook.dataTransfer.KafkaMessage("item-123", "http://api.pluggy.ai/transactions")
        );
        when(financialIntegrationService.findByLinkId("item-123")).thenReturn(integration);
        when(pluggyClient.fetchTransaction("http://api.pluggy.ai/transactions")).thenReturn(listResponse);
        when(accountRepository.findByPluggyAccountIdAndUser("pluggy-acc-123", user)).thenReturn(Optional.of(account));
        when(pluggyResponseMapper.mapPluggyToTransaction(transactionResponse)).thenReturn(mappedTransaction);
        when(transactionService.create(any())).thenReturn(mappedTransaction);

        webhookEventConsumer.consume(record);

        verify(transactionService).create(any(Transaction.class));
    }

    @Test
    @DisplayName("Should skip transaction when account not linked")
    void consume_accountNotLinked() throws Exception {
        String kafkaPayload = "{\"itemId\":\"item-123\",\"linkTransactions\":\"http://api.pluggy.ai/transactions\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("webhook-transactions", 0, 0, null, kafkaPayload);

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setAccountId("unknown-acc");
        transactionResponse.setAmount(new BigDecimal("50.00"));
        transactionResponse.setType("DEBIT");
        transactionResponse.setDate(ZonedDateTime.now());

        ListTransactionsResponse listResponse = new ListTransactionsResponse();
        listResponse.setResults(List.of(transactionResponse));

        when(objectMapper.readValue(eq(kafkaPayload), any(Class.class))).thenReturn(
                new com.gustavohenrique.financeApi.webhook.dataTransfer.KafkaMessage("item-123", "http://api.pluggy.ai/transactions")
        );
        when(financialIntegrationService.findByLinkId("item-123")).thenReturn(integration);
        when(pluggyClient.fetchTransaction("http://api.pluggy.ai/transactions")).thenReturn(listResponse);
        when(accountRepository.findByPluggyAccountIdAndUser("unknown-acc", user)).thenReturn(Optional.empty());

        webhookEventConsumer.consume(record);

        verify(transactionService, never()).create(any());
    }

    @Test
    @DisplayName("Should handle integration not found")
    void consume_integrationNotFound() throws Exception {
        String kafkaPayload = "{\"itemId\":\"unknown-item\",\"linkTransactions\":\"http://api.pluggy.ai/transactions\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("webhook-transactions", 0, 0, null, kafkaPayload);

        when(objectMapper.readValue(eq(kafkaPayload), any(Class.class))).thenReturn(
                new com.gustavohenrique.financeApi.webhook.dataTransfer.KafkaMessage("unknown-item", "http://api.pluggy.ai/transactions")
        );
        when(financialIntegrationService.findByLinkId("unknown-item")).thenThrow(new IntegrationLinkIdNotFoundException("unknown-item"));

        webhookEventConsumer.consume(record);

        verify(transactionService, never()).create(any());
    }

    @Test
    @DisplayName("Should handle integration with no user")
    void consume_noUser() throws Exception {
        String kafkaPayload = "{\"itemId\":\"item-123\",\"linkTransactions\":\"http://api.pluggy.ai/transactions\"}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("webhook-transactions", 0, 0, null, kafkaPayload);

        FinancialIntegration integrationNoUser = new FinancialIntegration();
        integrationNoUser.setLinkId("item-123");
        integrationNoUser.setUser(null);

        when(objectMapper.readValue(eq(kafkaPayload), any(Class.class))).thenReturn(
                new com.gustavohenrique.financeApi.webhook.dataTransfer.KafkaMessage("item-123", "http://api.pluggy.ai/transactions")
        );
        when(financialIntegrationService.findByLinkId("item-123")).thenReturn(integrationNoUser);

        webhookEventConsumer.consume(record);

        verify(pluggyClient, never()).fetchTransaction(any());
        verify(transactionService, never()).create(any());
    }
}
