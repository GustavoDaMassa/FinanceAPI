package com.gustavohenrique.financeApi.application.repositories;

import com.gustavohenrique.financeApi.application.interfaces.CategoryService;
import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByUser(User user);
}
