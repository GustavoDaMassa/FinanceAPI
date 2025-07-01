package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.CategoryService;
import com.gustavohenrique.financeApi.application.repositories.CategoryRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.CategoryNotFoundException;
import com.gustavohenrique.financeApi.exception.UserIDNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public List<Category> findAllByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserIDNotFoundException(userId));
        return categoryRepository.findAllByUser(user);
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category updatedCategory) {
        Category existing = findById(id);

        existing.setName(updatedCategory.getName());
        existing.setParent(updatedCategory.getParent());
        existing.setUser(updatedCategory.getUser());

        return categoryRepository.save(existing);
    }

    @Override
    @Transactional
    public Category delete(Long id) {
        Category existing = findById(id);
        existing.getSubcategories().size();
        categoryRepository.delete(existing);
        return existing;
    }
}
