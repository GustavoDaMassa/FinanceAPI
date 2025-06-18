package com.gustavohenrique.financeApi.graphql.inputs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserInput {

    @NotBlank
    private String name;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String password;
}
