package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.CategoryDTO;
import com.gustavohenrique.financeApi.graphql.inputs.CategoryInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private CategoryMapper categoryMapper;

    @BeforeEach
    void setUp() {
        categoryMapper = new CategoryMapper(new ModelMapper());
    }

    @Test
    @DisplayName("Should map Category to CategoryDTO")
    void toDto() {
        User user = new User();
        user.setId(1L);

        Category category = new Category();
        category.setId(10L);
        category.setName("Food");
        category.setUser(user);

        CategoryDTO dto = categoryMapper.toDto(category);

        assertEquals(10L, dto.getId());
        assertEquals("Food", dto.getName());
        assertEquals(1L, dto.getUserId());
    }

    @Test
    @DisplayName("Should map CategoryInput to Category")
    void fromInput() {
        CategoryInput input = new CategoryInput();
        input.setName("Transport");
        input.setUserId(1L);

        User user = new User();
        user.setId(1L);

        Category result = categoryMapper.fromInput(input, user);

        assertEquals("Transport", result.getName());
        assertEquals(user, result.getUser());
    }
}
