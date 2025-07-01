package com.gustavohenrique.financeApi.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String email) {
        super("The " + email+ " is unavailable");
    }
}