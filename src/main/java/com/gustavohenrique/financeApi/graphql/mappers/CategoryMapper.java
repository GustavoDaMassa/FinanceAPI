package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.CategoryDTO;
import com.gustavohenrique.financeApi.graphql.inputs.CategoryInput;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    private final ModelMapper modelMapper;

    public CategoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CategoryDTO toDto(Category category) {
        CategoryDTO dto = modelMapper.map(category, CategoryDTO.class);
        dto.setUserId(category.getUser().getId());
        return dto;
    }

    public Category fromInput(CategoryInput input, User user) {
        Category category = new Category();
        category.setName(input.getName());
        category.setUser(user);
        return category;
    }
}
