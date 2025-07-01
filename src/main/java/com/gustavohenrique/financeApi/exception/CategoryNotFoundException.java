package com.gustavohenrique.financeApi.exception;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Category not found with ID: " + id);
    }
}