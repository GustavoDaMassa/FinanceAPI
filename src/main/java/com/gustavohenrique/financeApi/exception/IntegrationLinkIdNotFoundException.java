package com.gustavohenrique.financeApi.exception;

import org.apache.kafka.common.protocol.types.Field;

public class IntegrationLinkIdNotFoundException extends NotFoundException {
    public IntegrationLinkIdNotFoundException(String linkId) {
        super("Integration not found with ID: " + linkId);
    }
}