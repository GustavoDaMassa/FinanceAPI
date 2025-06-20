package com.gustavohenrique.financeApi.application.interfaces;

import com.gustavohenrique.financeApi.domain.models.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface BalanceCalculatorService {
    BigDecimal calculate(List<Transaction> transactions);
}

