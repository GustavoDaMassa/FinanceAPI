package com.gustavohenrique.financeApi.webhook;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;
import com.gustavohenrique.financeApi.webhook.service.PluggyResponseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PluggyResponseMapperTest {

    private PluggyResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PluggyResponseMapper();
    }

    @Test
    @DisplayName("Should map CREDIT to INFLOW")
    void mapCredit() {
        TransactionResponse response = new TransactionResponse();
        response.setType("CREDIT");
        response.setAmount(new BigDecimal("100.50"));
        response.setDescription("Salary");
        response.setDate(ZonedDateTime.now());

        Transaction result = mapper.mapPluggyToTransaction(response);

        assertEquals(TransactionType.INFLOW, result.getType());
        assertEquals(new BigDecimal("100.50"), result.getAmount());
        assertEquals("Salary", result.getDescription());
    }

    @Test
    @DisplayName("Should map DEBIT to OUTFLOW")
    void mapDebit() {
        TransactionResponse response = new TransactionResponse();
        response.setType("DEBIT");
        response.setAmount(new BigDecimal("-50.00"));
        response.setDescription("Purchase");
        response.setDate(ZonedDateTime.now());

        Transaction result = mapper.mapPluggyToTransaction(response);

        assertEquals(TransactionType.OUTFLOW, result.getType());
        assertEquals(new BigDecimal("50.00"), result.getAmount());
    }

    @Test
    @DisplayName("Should throw on invalid type")
    void mapInvalidType() {
        TransactionResponse response = new TransactionResponse();
        response.setType("INVALID");
        response.setAmount(new BigDecimal("10.00"));
        response.setDescription("test");
        response.setDate(ZonedDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> mapper.mapPluggyToTransaction(response));
    }
}
