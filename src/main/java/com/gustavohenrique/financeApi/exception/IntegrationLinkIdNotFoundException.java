package com.gustavohenrique.financeApi.exception;

public class IntegrationLinkIdNotFoundException extends NotFoundException {
    public IntegrationLinkIdNotFoundException(String linkId) {
        super("Integration not found with ID: " + linkId);
    }
}