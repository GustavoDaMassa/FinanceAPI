package com.gustavohenrique.financeApi.graphql.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryInput {

    @NotBlank
    private String name;

    @NotNull
    private Long userId;
}
