package com.gustavohenrique.financeApi.webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavohenrique.financeApi.webhook.dataTransfer.KafkaMessage;
import com.gustavohenrique.financeApi.webhook.producer.WebhookEventProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebhookEventProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private WebhookEventProducer webhookEventProducer;

    @Test
    @DisplayName("Should serialize and send KafkaMessage")
    void send_success() throws JsonProcessingException {
        KafkaMessage message = new KafkaMessage("item-123", "http://link.com/transactions");
        when(objectMapper.writeValueAsString(message)).thenReturn("{\"itemId\":\"item-123\"}");

        webhookEventProducer.send(message);

        verify(kafkaTemplate).send("webhook-transactions", "{\"itemId\":\"item-123\"}");
    }

    @Test
    @DisplayName("Should handle serialization error gracefully")
    void send_serializationError() throws JsonProcessingException {
        KafkaMessage message = new KafkaMessage("item-123", "http://link.com");
        when(objectMapper.writeValueAsString(message)).thenThrow(new JsonProcessingException("error") {});

        webhookEventProducer.send(message);

        verify(kafkaTemplate, never()).send(anyString(), anyString());
    }
}
