package com.gustavohenrique.financeApi.exception;

public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException(Long id) {
        super("Account not found with ID: " + id);
    }
}