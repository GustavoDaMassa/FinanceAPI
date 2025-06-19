package com.gustavohenrique.financeApi.application.results;

import com.gustavohenrique.financeApi.domain.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionQueryResult {
    private List<Transaction> transactions;
    private BigDecimal balance;
}

