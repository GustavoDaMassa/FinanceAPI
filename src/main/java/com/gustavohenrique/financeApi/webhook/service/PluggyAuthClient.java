package com.gustavohenrique.financeApi.webhook.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
public class PluggyAuthClient {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.pluggy.ai")
            .build();

    public String getAccessToken(String clientId, String clientSecret) {
        var response = webClient.post()
                .uri("/auth")
                .bodyValue(new AuthRequest(clientId, clientSecret))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();

        log.info("Pluggy access token obtained successfully");
        return response != null ? response.apiKey() : null;
    }

    public record AuthRequest(String clientId, String clientSecret) {}
    public record AuthResponse(String apiKey) {}
}
