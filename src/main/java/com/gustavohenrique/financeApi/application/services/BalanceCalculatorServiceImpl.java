package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.BalanceCalculatorService;
import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BalanceCalculatorServiceImpl implements BalanceCalculatorService {

    @Override
    public BigDecimal calculate(List<Transaction> transactions) {
        return transactions.stream()
                .map(t -> t.getType() == TransactionType.INFLOW
                        ? t.getAmount()
                        : t.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
