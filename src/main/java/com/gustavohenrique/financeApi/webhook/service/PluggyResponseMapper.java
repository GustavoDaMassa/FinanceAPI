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
             TransactionType.fromPluggy(response.getType()),
             response.getDescription(),
             null,
             null,
             response.getDate().toLocalDate(),
             null,
             response.getId(),
             null
        );
    }
}
