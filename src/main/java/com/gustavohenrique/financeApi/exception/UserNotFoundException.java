package com.gustavohenrique.financeApi.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String email) {
        super("User not found with Email: " + email);
    }
}