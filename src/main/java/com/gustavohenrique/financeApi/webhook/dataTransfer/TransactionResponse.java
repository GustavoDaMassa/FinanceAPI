package com.gustavohenrique.financeApi.webhook.dataTransfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponse {
    private String description;
    private BigDecimal amount;
    private String type;
}
