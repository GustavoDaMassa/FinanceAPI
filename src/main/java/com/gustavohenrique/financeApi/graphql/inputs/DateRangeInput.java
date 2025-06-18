package com.gustavohenrique.financeApi.graphql.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DateRangeInput {

    @NotBlank
    private String startDate;
    private String endDate;
}
