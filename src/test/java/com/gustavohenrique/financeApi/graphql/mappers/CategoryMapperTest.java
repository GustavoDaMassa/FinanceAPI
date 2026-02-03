package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.CategoryDTO;
import com.gustavohenrique.financeApi.graphql.inputs.CategoryInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;

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
        category.setParent(null);
        category.setSubcategories(null);

        CategoryDTO dto = categoryMapper.toDto(category);

        assertEquals(10L, dto.getId());
        assertEquals("Food", dto.getName());
        assertEquals(1L, dto.getUserId());
        assertNull(dto.getParentId());
    }

    @Test
    @DisplayName("Should map Category with parent and subcategories")
    void toDto_withParentAndSubcategories() {
        User user = new User();
        user.setId(1L);

        Category parent = new Category();
        parent.setId(5L);
        parent.setName("Food");
        parent.setUser(user);

        Category sub = new Category();
        sub.setId(11L);
        sub.setName("Restaurant");
        sub.setUser(user);
        sub.setParent(parent);
        sub.setSubcategories(null);

        parent.setSubcategories(List.of(sub));

        CategoryDTO dto = categoryMapper.toDto(parent);

        assertEquals(5L, dto.getId());
        assertNotNull(dto.getSubcategories());
        assertEquals(1, dto.getSubcategories().size());
        assertEquals("Restaurant", dto.getSubcategories().get(0).getName());
        assertEquals(5L, dto.getSubcategories().get(0).getParentId());
    }

    @Test
    @DisplayName("Should map CategoryInput to Category")
    void fromInput() {
        CategoryInput input = new CategoryInput();
        input.setName("Transport");
        input.setUserId(1L);

        User user = new User();
        user.setId(1L);

        Category parent = new Category();
        parent.setId(5L);

        Category result = categoryMapper.fromInput(input, user, parent);

        assertEquals("Transport", result.getName());
        assertEquals(user, result.getUser());
        assertEquals(parent, result.getParent());
    }
}
