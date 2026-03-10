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
}
