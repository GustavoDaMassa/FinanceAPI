package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.CategoryDTO;
import com.gustavohenrique.financeApi.graphql.inputs.CategoryInput;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {

    private final ModelMapper modelMapper;

    public CategoryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CategoryDTO toDto(Category category) {
        CategoryDTO dto = modelMapper.map(category, CategoryDTO.class);
        dto.setUserId(category.getUser().getId());

        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
        }

        if (category.getSubcategories() != null) {
            List<CategoryDTO> subDtos = category.getSubcategories()
                    .stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
            dto.setSubcategories(subDtos);
        }

        return dto;
    }

    public Category fromInput(CategoryInput input, User user, Category parentCategory) {
        Category category = new Category();
        category.setName(input.getName());
        category.setUser(user);
        category.setParent(parentCategory);
        return category;
    }
}
