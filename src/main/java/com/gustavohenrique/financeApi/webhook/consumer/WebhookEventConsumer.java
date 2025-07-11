package com.gustavohenrique.financeApi.webhook.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.TransactionRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.webhook.services.PluggyClient;
import com.gustavohenrique.financeApi.webhook.models.KafkaMessage;
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
    private final AccountRepository accountRepository;
    private final FinancialIntegrationService financialIntegrationService;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    private static final Long DEFAULT_USER_ID = 5L;
    private static final Long DEFAULT_ACCOUNT_ID = 11L;

    @KafkaListener(topics = "webhook-transactions", groupId = "finance-consumer")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            KafkaMessage event = objectMapper.readValue(record.value(), KafkaMessage.class);

            log.info("📥 Mensagem recebida: {}", event);

            FinancialIntegration integration = financialIntegrationService
                    .resolveIntegrationByLinkId(event.getItemId(), DEFAULT_USER_ID,DEFAULT_ACCOUNT_ID, AggregatorType.PLUGGY);


            Transaction transaction = pluggyClient.fetchTransaction(event.getEventId(), DEFAULT_ACCOUNT_ID);

            transactionRepository.save(transaction);

            log.info("💾 Transação persistida com sucesso: {}", transaction);
        } catch (Exception e) {
            log.error("❌ Erro ao processar evento do Kafka", e);
        }
    }
}
