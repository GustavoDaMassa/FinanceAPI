package com.gustavohenrique.financeApi.security.dto;

import com.gustavohenrique.financeApi.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Long userId;
    private String email;
    private String name;
    private Role role;

    public LoginResponse(String token, Long userId, String email, String name, Role role) {
        this.token = token;
        this.type = "Bearer";
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }
}
