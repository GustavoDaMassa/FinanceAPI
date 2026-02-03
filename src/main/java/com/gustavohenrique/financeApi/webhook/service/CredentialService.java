package com.gustavohenrique.financeApi.webhook.service;

import com.gustavohenrique.financeApi.webhook.dataTransfer.ClientCredencials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {

    @Value("${pluggy.client-id}")
    private String clientId;

    @Value("${pluggy.client-secret}")
    private String clientSecret;

    public ClientCredencials readCredentials() {
        ClientCredencials credentials = new ClientCredencials();
        credentials.setClientId(clientId);
        credentials.setClientSecret(clientSecret);
        return credentials;
    }
}
