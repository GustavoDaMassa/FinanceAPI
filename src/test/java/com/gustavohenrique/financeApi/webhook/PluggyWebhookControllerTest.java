package com.gustavohenrique.financeApi.webhook;

import com.gustavohenrique.financeApi.webhook.controllers.PluggyWebhookController;
import com.gustavohenrique.financeApi.webhook.dataTransfer.KafkaMessage;
import com.gustavohenrique.financeApi.webhook.producer.WebhookEventProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PluggyWebhookControllerTest {

    @Mock
    private WebhookEventProducer webhookEventProducer;

    @InjectMocks
    private PluggyWebhookController pluggyWebhookController;

    @Test
    @DisplayName("Should send message when payload has itemId")
    void receiveWebhook_withItemId() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("itemId", "item-123");
        payload.put("createdTransactionsLink", "http://link.com/transactions");

        pluggyWebhookController.receiveWebhook(payload);

        verify(webhookEventProducer).send(any(KafkaMessage.class));
    }

    @Test
    @DisplayName("Should not send message when payload has no itemId")
    void receiveWebhook_withoutItemId() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", "some-event");

        pluggyWebhookController.receiveWebhook(payload);

        verify(webhookEventProducer, never()).send(any());
    }
}
