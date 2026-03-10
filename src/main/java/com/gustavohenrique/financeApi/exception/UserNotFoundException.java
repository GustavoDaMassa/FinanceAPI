package com.gustavohenrique.financeApi.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }

    public UserNotFoundException(Long id) {
        super("User not found with ID: " + id);
    }
}