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

    public static TransactionType fromOFX(String ofxType, BigDecimal amount) {
        return switch (ofxType.toUpperCase()) {
            case "CREDIT", "INT", "DIV", "DEP", "DIRECTDEP" -> INFLOW;
            case "DEBIT", "ATM", "POS", "FEE", "SRVCHG", "CHECK", "PAYMENT", "DIRECTDEBIT" -> OUTFLOW;
            default -> amount.compareTo(BigDecimal.ZERO) >= 0 ? INFLOW : OUTFLOW;
        };
    }
}
