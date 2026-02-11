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
     return new Transaction(null,
             new BigDecimal(String.valueOf(response.getAmount())).abs(),
             mapType(response.getType()),
             response.getDescription(),
             null,
             null,
             response.getDate().toLocalDate(),
            null,
             null
        );
}
    private TransactionType mapType(String pluggyType) {
        return switch (pluggyType.toUpperCase()) {
            case "CREDIT" -> TransactionType.INFLOW;
            case "DEBIT" -> TransactionType.OUTFLOW;
            default -> throw new IllegalArgumentException("Tipo de transação inválido: " + pluggyType);
        };
    }
}
