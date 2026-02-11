package com.gustavohenrique.financeApi.graphql.dtos;

import lombok.Data;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Long userId;
}
