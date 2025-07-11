package com.gustavohenrique.financeApi.webhook.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavohenrique.financeApi.webhook.models.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void send(KafkaMessage kafkaMessage) {
        try {
            String payload = objectMapper.writeValueAsString(kafkaMessage);
            String TOPIC_NAME = "webhook-transactions";
            kafkaTemplate.send(TOPIC_NAME, payload);
            log.info("📤 Evento enviado ao Kafka: {}", payload);
        } catch (JsonProcessingException e) {
            log.error("❌ Erro ao serializar KafkaMessage", e);
        }
    }
}
