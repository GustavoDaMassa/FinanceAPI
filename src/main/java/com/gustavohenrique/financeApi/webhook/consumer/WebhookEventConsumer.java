package com.gustavohenrique.financeApi.webhook.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavohenrique.financeApi.application.interfaces.AccountService;
import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.application.interfaces.TransactionWriter;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.IntegrationLinkIdNotFoundException;
import com.gustavohenrique.financeApi.webhook.dataTransfer.KafkaMessage;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ListTransactionsResponse;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;
import com.gustavohenrique.financeApi.webhook.service.PluggyResponseMapper;
import com.gustavohenrique.financeApi.webhook.service.PluggyClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookEventConsumer {

    private final ObjectMapper objectMapper;
    private final PluggyClient pluggyClient;
    private final FinancialIntegrationService financialIntegrationService;
    private final TransactionWriter transactionService;
    private final AccountService accountService;
    private final PluggyResponseMapper pluggyResponseMapper;

    @KafkaListener(topics = "webhook-transactions", groupId = "finance-consumer")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            KafkaMessage event = objectMapper.readValue(record.value(), KafkaMessage.class);
            log.info("📥 Inbound webhook event received: {}", event);

            FinancialIntegration integration = financialIntegrationService.findByLinkId(event.getItemId());
            User user = integration.getUser();

            if (user == null) {
                log.error("❌ Integration with linkId {} has no associated user.", event.getItemId());
                return;
            }

            ListTransactionsResponse transactionsResponse = pluggyClient.fetchTransaction(event.getLinkTransactions());

            for (TransactionResponse result : transactionsResponse.getResults()) {
                if (transactionService.existsByExternalId(result.getId())) {
                    log.info("⏭️ Transaction with externalId {} already exists. Skipping.", result.getId());
                    continue;
                }

                accountService.findByPluggyAccountIdAndUser(result.getAccountId(), user)
                        .ifPresentOrElse(
                                account -> {
                                    Transaction transaction = pluggyResponseMapper.mapPluggyToTransaction(result);
                                    transaction.setAccount(account);
                                    transactionService.create(transaction);
                                    log.info("💾 Transaction {} successfully persisted for account {}.", transaction.getId(), account.getId());
                                },
                                () -> log.warn("⚠️ Account with pluggyAccountId {} not found for user {}. Skipping transaction.", result.getAccountId(), user.getId())
                        );
            }
        } catch (JsonProcessingException e) {
            log.error("❌ Error deserializing Kafka message: {}", record.value(), e);
        } catch (IntegrationLinkIdNotFoundException e) {
            log.error("❌ FinancialIntegration not found for linkId in Kafka message.", e);
        } catch (Exception e) {
            log.error("❌ An unexpected error occurred while processing webhook event.", e);
        }
    }
}
