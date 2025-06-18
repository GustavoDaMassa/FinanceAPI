package com.gustavohenrique.financeApi.graphql.dtos;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import lombok.Data;

@Data
public class TransactionDTO {
    private Long id;
    private String amount;
    private TransactionType type;
    private String description;
    private String source;
    private String destination;
    private String transactionDate;

    private Long categoryId;
    private Long subcategoryId;
    private Long accountId;
}
