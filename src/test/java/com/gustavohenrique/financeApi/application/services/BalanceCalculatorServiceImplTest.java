package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BalanceCalculatorServiceImplTest {

    private final BalanceCalculatorServiceImpl balanceCalculator = new BalanceCalculatorServiceImpl();

    @Test
    @DisplayName("Should return zero when transaction list is empty")
    void calculate_emptyList_shouldReturnZero() {
        var result = balanceCalculator.calculate(List.of());
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @DisplayName("Should sum all INFLOW transactions correctly")
    void calculate_onlyInflows_shouldReturnTotal() {
        var t1 = new Transaction(1L, new BigDecimal("100.00"), TransactionType.INFLOW,
                "Salary", null, null, LocalDate.now(), null, null);
        var t2 = new Transaction(2L, new BigDecimal("50.00"), TransactionType.INFLOW,
                "Bonus", null, null, LocalDate.now(), null, null);

        var result = balanceCalculator.calculate(List.of(t1, t2));
        assertEquals(new BigDecimal("150.00"), result);
    }

    @Test
    @DisplayName("Should subtract all OUTFLOW transactions correctly")
    void calculate_onlyOutflows_shouldReturnNegativeTotal() {
        var t1 = new Transaction(1L, new BigDecimal("30.00"), TransactionType.OUTFLOW,
                "Groceries", null, null, LocalDate.now(), null, null);
        var t2 = new Transaction(2L, new BigDecimal("20.00"), TransactionType.OUTFLOW,
                "Transport", null, null, LocalDate.now(), null, null);

        var result = balanceCalculator.calculate(List.of(t1, t2));
        assertEquals(new BigDecimal("-50.00"), result);
    }

    @Test
    @DisplayName("Should calculate net balance with both INFLOW and OUTFLOW")
    void calculate_mixedTransactions_shouldReturnNetBalance() {
        var inflow = new Transaction(1L, new BigDecimal("200.00"), TransactionType.INFLOW,
                "Freelance", null, null, LocalDate.now(), null, null);
        var outflow = new Transaction(2L, new BigDecimal("70.00"), TransactionType.OUTFLOW,
                "Bills", null, null, LocalDate.now(), null, null);

        var result = balanceCalculator.calculate(List.of(inflow, outflow));
        assertEquals(new BigDecimal("130.00"), result);
    }
}
