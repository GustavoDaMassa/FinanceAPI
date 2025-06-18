package com.gustavohenrique.financeApi.graphql.dtos;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String email;
}
