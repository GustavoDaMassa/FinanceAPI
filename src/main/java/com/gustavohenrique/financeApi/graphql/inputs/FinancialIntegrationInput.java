package com.gustavohenrique.financeApi.graphql.inputs;

import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FinancialIntegrationInput {

    @NotNull
    private AggregatorType aggregator;

    @NotBlank
    private String linkId;

    @NotNull
    private Long userId;
}
