package com.gustavohenrique.financeApi.webhook.consumer;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class PluggyConfig {

    private String clientId = "32804964-082c-4759-b0e6-a1a198b277cf";

    private String clientSecret = "a1ee493b-c8fe-4b49-bf21-06aa45703c3c";

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
