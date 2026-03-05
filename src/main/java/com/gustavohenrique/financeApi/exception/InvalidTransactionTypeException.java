package com.gustavohenrique.financeApi.exception;

public class InvalidTransactionTypeException extends BadRequestException {
    public InvalidTransactionTypeException(String type) {
        super("Invalid transaction type: " + type);
    }
}