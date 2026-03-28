package com.gustavohenrique.financeApi.importer.mapper;

import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.importer.dto.ParsedTransaction;
import org.springframework.stereotype.Component;

@Component
public class ParsedTransactionMapper {

    public Transaction toTransaction(ParsedTransaction parsed, Account account) {
        return Transaction.builder()
                .amount(parsed.getAmount())
                .type(parsed.getType())
                .description(parsed.getDescription())
                .transactionDate(parsed.getDate())
                .externalId(parsed.getExternalId())
                .account(account)
                .build();
    }
}
