package com.gustavohenrique.financeApi.webhook.service;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class PluggyResponseMapper {

    public Transaction mapPluggyToTransaction(TransactionResponse response){
        return Transaction.builder()
                .amount(new BigDecimal(String.valueOf(response.getAmount())).abs())
                .type(TransactionType.fromPluggy(response.getType()))
                .description(response.getDescription())
                .transactionDate(response.getDate().toLocalDate())
                .externalId(response.getId())
                .build();
    }
}
