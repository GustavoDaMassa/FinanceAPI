package com.gustavohenrique.financeApi.exception;

public class IntegrationNotFoundException extends NotFoundException {
    public IntegrationNotFoundException(Long id) {
        super("Integration not found with ID: " + id);
    }
}