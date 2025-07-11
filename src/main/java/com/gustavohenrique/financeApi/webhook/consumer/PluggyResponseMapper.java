package com.gustavohenrique.financeApi.webhook.mapper;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.webhook.models.TransactionResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class PluggyResponseMapper {

    public Transaction mapPluggyToTransaction(TransactionResponse response, Account account){
     return new Transaction(
                null,
                        new BigDecimal(response.amount()),
    mapType(response.type()),
            response.description(),
            response.description(),
            response.description(),
            LocalDate.parse(response.date()),
            null, null,
    account
        );
}

private TransactionType mapType(String pluggyType) {
    return pluggyType.equalsIgnoreCase("INFLOW") ? TransactionType.INFLOW : TransactionType.OUTFLOW;
}
}
