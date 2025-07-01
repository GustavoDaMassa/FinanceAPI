package com.gustavohenrique.financeApi.exception;

public class UserIDNotFoundException extends NotFoundException {
    public UserIDNotFoundException(Long id) {
        super("User not found with ID: " + id);
    }
}