package com.gustavohenrique.financeApi.graphql.inputs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountInput {

    @NotBlank
    private String accountName;
    private String institution;
    private String type;
    private String balance;

    @NotBlank
    private Long userId;
    private Long integrationId;

}
