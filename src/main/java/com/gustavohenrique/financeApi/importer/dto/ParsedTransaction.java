package com.gustavohenrique.financeApi.importer.dto;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class ParsedTransaction {
    private String externalId;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDate date;
}
