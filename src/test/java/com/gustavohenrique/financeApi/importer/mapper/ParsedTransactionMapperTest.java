package com.gustavohenrique.financeApi.importer.mapper;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.importer.dto.ParsedTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ParsedTransactionMapperTest {

    private ParsedTransactionMapper mapper;
    private Account account;
    private ParsedTransaction parsed;

    @BeforeEach
    void setUp() {
        mapper = new ParsedTransactionMapper();

        account = Account.builder().id(1L).accountName("Conta Corrente").build();

        parsed = ParsedTransaction.builder()
                .externalId("FIT-001")
                .amount(new BigDecimal("250.00"))
                .type(TransactionType.OUTFLOW)
                .description("PAGAMENTO FATURA")
                .date(LocalDate.of(2024, 3, 10))
                .build();
    }

    @Test
    @DisplayName("Should map all fields from ParsedTransaction to Transaction")
    void toTransaction_mapsAllFields() {
        Transaction result = mapper.toTransaction(parsed, account);

        assertEquals(new BigDecimal("250.00"), result.getAmount());
        assertEquals(TransactionType.OUTFLOW, result.getType());
        assertEquals("PAGAMENTO FATURA", result.getDescription());
        assertEquals(LocalDate.of(2024, 3, 10), result.getTransactionDate());
        assertEquals("FIT-001", result.getExternalId());
    }

    @Test
    @DisplayName("Should associate the provided account to the transaction")
    void toTransaction_setsAccount() {
        Transaction result = mapper.toTransaction(parsed, account);

        assertNotNull(result.getAccount());
        assertEquals(1L, result.getAccount().getId());
    }

    @Test
    @DisplayName("Should map transaction with null description")
    void toTransaction_nullDescription() {
        ParsedTransaction withoutDescription = ParsedTransaction.builder()
                .externalId("FIT-002")
                .amount(new BigDecimal("100.00"))
                .type(TransactionType.INFLOW)
                .description(null)
                .date(LocalDate.of(2024, 3, 11))
                .build();

        Transaction result = mapper.toTransaction(withoutDescription, account);

        assertNull(result.getDescription());
    }
}
