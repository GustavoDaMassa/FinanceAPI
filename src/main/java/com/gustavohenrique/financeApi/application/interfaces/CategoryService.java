package com.gustavohenrique.financeApi.application.interfaces;

import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.User;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface CategoryService {
    Category findById(Long id);

    List<Category> findAllByUserId(Long userId);

    User findUserById(@NotNull Long userId);

    Category create(Category category);

    Category update(Long id, Category category);

    Category delete(Long id);
}
