package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.repositories.CategoryRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.CategoryNotFoundException;
import com.gustavohenrique.financeApi.exception.UserIDNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Gustavo", "gustavo@test.com", "123456", null, null);
        category = new Category(1L, "Alimentação", user, null, List.of());
    }

    @Test
    @DisplayName("Should return category when ID exists")
    void findById_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals("Alimentação", result.getName());
    }

    @Test
    @DisplayName("Should throw when category ID does not exist")
    void findById_notFound_shouldThrow() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findById(1L));
    }

    @Test
    @DisplayName("Should return all categories by user ID")
    void findAllByUserId_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(categoryRepository.findAllByUser(user)).thenReturn(List.of(category));

        List<Category> result = categoryService.findAllByUserId(1L);

        assertEquals(1, result.size());
        assertEquals("Alimentação", result.getFirst().getName());
    }

    @Test
    @DisplayName("Should throw when user ID is not found")
    void findAllByUserId_notFound_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserIDNotFoundException.class, () -> categoryService.findAllByUserId(1L));
    }

    @Test
    @DisplayName("Should create and return category")
    void create_success() {
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.create(category);

        assertNotNull(result);
        assertEquals("Alimentação", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Should update category when ID exists")
    void update_success() {
        Category updatedCategory = new Category(null, "Transporte", user, null, null);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any())).thenReturn(updatedCategory);

        Category result = categoryService.update(1L, updatedCategory);

        assertEquals("Transporte", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Should throw when updating non-existing category")
    void update_notFound_shouldThrow() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.update(1L, category));
    }

    @Test
    @DisplayName("Should delete category when ID exists")
    void delete_success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Category result = categoryService.delete(1L);

        assertEquals(category, result);
        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("Should throw when deleting non-existing category")
    void delete_notFound_shouldThrow() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.delete(1L));
    }
}
