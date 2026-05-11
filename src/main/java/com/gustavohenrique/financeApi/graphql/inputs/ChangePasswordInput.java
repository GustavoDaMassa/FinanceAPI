package com.gustavohenrique.financeApi.graphql.inputs;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordInput {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}
