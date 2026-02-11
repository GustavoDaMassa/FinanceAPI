package com.gustavohenrique.financeApi.graphql.inputs;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TransactionInput {

    @NotBlank
    private String amount;

    @NotNull
    private TransactionType type;

    private String description;
    private String source;
    private String destination;

    @NotBlank
    private String transactionDate;

    @NotNull
    private Long accountId;

    private Long categoryId;
}
