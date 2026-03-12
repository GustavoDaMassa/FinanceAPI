package com.gustavohenrique.financeApi.domain.enums;

import java.math.BigDecimal;

public enum TransactionType {
    INFLOW {
        @Override
        public BigDecimal apply(BigDecimal amount) { return amount; }
    },
    OUTFLOW {
        @Override
        public BigDecimal apply(BigDecimal amount) { return amount.negate(); }
    };

    public abstract BigDecimal apply(BigDecimal amount);

    public static TransactionType fromPluggy(String pluggyType) {
        return switch (pluggyType.toUpperCase()) {
            case "CREDIT" -> INFLOW;
            case "DEBIT"  -> OUTFLOW;
            default -> throw new IllegalArgumentException("Tipo de transação inválido: " + pluggyType);
        };
    }
}
