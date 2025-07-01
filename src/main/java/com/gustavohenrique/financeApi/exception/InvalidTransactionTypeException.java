package com.gustavohenrique.financeApi.exception;

public class InvalidTransactionTypeException extends RuntimeException {
    public InvalidTransactionTypeException(String type) {
        super("Invalid transaction type: " + type);
    }
}