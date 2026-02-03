package com.gustavohenrique.financeApi.webhook;

import com.gustavohenrique.financeApi.webhook.dataTransfer.ClientCredencials;
import com.gustavohenrique.financeApi.webhook.service.CredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class CredentialServiceTest {

    private CredentialService credentialService;

    @BeforeEach
    void setUp() {
        credentialService = new CredentialService();
        ReflectionTestUtils.setField(credentialService, "clientId", "test-client-id");
        ReflectionTestUtils.setField(credentialService, "clientSecret", "test-client-secret");
    }

    @Test
    @DisplayName("Should return credentials from properties")
    void readCredentials() {
        ClientCredencials creds = credentialService.readCredentials();

        assertEquals("test-client-id", creds.getClientId());
        assertEquals("test-client-secret", creds.getClientSecret());
    }
}
