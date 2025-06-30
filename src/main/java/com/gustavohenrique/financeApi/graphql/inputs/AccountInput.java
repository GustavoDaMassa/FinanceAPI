package com.gustavohenrique.financeApi.graphql.inputs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountInput {

    @NotBlank
    private String accountName;
    private String institution;
    private String type;

    @NotNull
    private Long userId;
    private Long integrationId;

}
