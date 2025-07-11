package com.gustavohenrique.financeApi.webhook;


import com.gustavohenrique.financeApi.webhook.kafka.WebhookEventProducer;
import com.gustavohenrique.financeApi.webhook.models.KafkaMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PluggyWebhookController {

    private final WebhookEventProducer webhookEventProducer;

    @PostMapping
    public void receiveWebhook(@RequestBody Map<String, Object> payload) {
        log.info("🔔 Webhook recebido: {}", payload);

        String itemId = (String) payload.get("itemId");
        String eventId = (String) payload.get("eventId");

        if (itemId != null && eventId != null) {
            KafkaMessage message = new KafkaMessage(itemId, eventId);
            webhookEventProducer.send(message);
        } else {
            log.warn("⚠️ Campos itemId ou eventId ausentes no payload: {}", payload);
        }
    }
}