package com.gustavohenrique.financeApi.webhook.dataTransfer;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class ClientCredencials {
    private String clientId;
    private String clientSecret;
}
