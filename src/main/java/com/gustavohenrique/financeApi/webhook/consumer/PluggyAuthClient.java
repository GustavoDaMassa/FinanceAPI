package com.gustavohenrique.financeApi.webhook.services;

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
                .uri("/auth/token")
                .bodyValue(new AuthRequest(clientId, clientSecret))
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();

        log.info("🔑 Token de acesso obtido com sucesso");
        return response != null ? response.accessToken() : null;
    }

    public record AuthRequest(String clientId, String clientSecret) {}
    public record AuthResponse(String accessToken, String expiresIn, String tokenType) {}
}
