package com.gustavohenrique.financeApi.webhook.models;

public record TransactionResponse(
        String id,
        String description,
        String type,
        String amount,
        String date
) {}