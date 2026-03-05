package com.gustavohenrique.financeApi.exception;

public class EmailAlreadyExistException extends BadRequestException {
    public EmailAlreadyExistException(String email) {
        super("The " + email+ " is unavailable");
    }
}