package com.gustavohenrique.financeApi.exception;

public class InvalidPasswordException extends BadRequestException {
    public InvalidPasswordException() {
        super("Current password is incorrect");
    }
}
