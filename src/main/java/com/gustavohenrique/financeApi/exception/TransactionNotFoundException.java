package com.gustavohenrique.financeApi.exception;

public class TransactionNotFoundException extends NotFoundException {
    public TransactionNotFoundException(Long id) {
        super("Transaction not found with ID: " + id);
    }
}