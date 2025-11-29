package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.CategoryService;
import com.gustavohenrique.financeApi.application.interfaces.UserService;
import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.CategoryDTO;
import com.gustavohenrique.financeApi.graphql.inputs.CategoryInput;
import com.gustavohenrique.financeApi.graphql.mappers.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CategoryResolver {

    private final CategoryService categoryService;
    private final UserService userService;
    private final CategoryMapper categoryMapper;

    // ===== Queries =====

    @QueryMapping
    public CategoryDTO findCategoryById(@Argument Long id) {
        Category category = categoryService.findById(id);
        return categoryMapper.toDto(category);
    }

    @QueryMapping
    public List<CategoryDTO> listCategoriesByUser(@Argument Long userId) {
        return categoryService.findAllByUserId(userId)
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    // ===== Mutations =====

    @MutationMapping
    public CategoryDTO createCategory(@Argument CategoryInput input) {
        User user = userService.findById(input.getUserId());
        Category parent = input.getParentId() != null ? categoryService.findById(input.getParentId()) : null;

        Category created = categoryService.create(categoryMapper.fromInput(input, user, parent));
        return categoryMapper.toDto(created);
    }

    @MutationMapping
    public CategoryDTO updateCategory(@Argument Long id, @Argument CategoryInput input) {
        User user = userService.findById(input.getUserId());
        Category parent = input.getParentId() != null ? categoryService.findById(input.getParentId()) : null;

        Category updated = categoryService.update(id, categoryMapper.fromInput(input, user, parent));
        return categoryMapper.toDto(updated);
    }

    @MutationMapping
    public CategoryDTO deleteCategory(@Argument Long id) {
        Category deleted = categoryService.delete(id);
        return categoryMapper.toDto(deleted);
    }
}
