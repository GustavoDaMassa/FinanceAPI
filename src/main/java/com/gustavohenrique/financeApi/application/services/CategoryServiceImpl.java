package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.CategoryService;
import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{
    @Override
    public Category findById(Long id) {
        return null;
    }

    @Override
    public List<Category> findAllByUserId(Long userId) {
        return List.of();
    }

    @Override
    public User findUserById(Long userId) {
        return null;
    }

    @Override
    public Category create(Category category) {
        return null;
    }

    @Override
    public Category update(Long id, Category category) {
        return null;
    }

    @Override
    public Category delete(Long id) {
        return null;
    }
}
