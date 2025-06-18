package com.gustavohenrique.financeApi.graphql.dtos;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private Long userId;
    private Long parentId;

    private List<CategoryDTO> subcategories;
}
