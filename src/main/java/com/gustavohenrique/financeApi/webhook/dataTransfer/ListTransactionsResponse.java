package com.gustavohenrique.financeApi.webhook.dataTransfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListTransactionsResponse {
    private List<TransactionResponse> results;
}
