package com.gustavohenrique.financeApi.webhook.controllers;


import com.gustavohenrique.financeApi.webhook.dataTransfer.KafkaMessage;
import com.gustavohenrique.financeApi.webhook.producer.WebhookEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhook/pluggy")
@RequiredArgsConstructor
public class PluggyWebhookController {

    private final WebhookEventProducer webhookEventProducer;


    @GetMapping
    public String healthCheck() {
        log.info("🔍 Pluggy verificando endpoint via GET");
        return "Webhook endpoint is active!";
    }


    @PostMapping
    public void receiveWebhook(@RequestBody Map<String, Object> payload) {
        log.info("🔔 Webhook recebido: {}", payload);

        String itemId = (String) payload.get("itemId");
        String linkTransactions = (String) payload.get("createdTransactionsLink");

        if (itemId != null) {
            KafkaMessage message = new KafkaMessage(itemId, linkTransactions);
            webhookEventProducer.send(message);
        } else {
            log.warn("⚠️ Campos itemId ou eventId ausentes no payload: {}", payload);
        }
    }
}