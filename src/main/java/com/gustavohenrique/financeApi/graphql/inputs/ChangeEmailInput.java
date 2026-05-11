package com.gustavohenrique.financeApi.graphql.inputs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeEmailInput {

    @NotBlank
    private String currentPassword;

    @Email
    @NotBlank
    private String newEmail;
}
