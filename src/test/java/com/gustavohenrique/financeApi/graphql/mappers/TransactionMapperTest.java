package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionDTO;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionListWithBalanceDTO;
import com.gustavohenrique.financeApi.graphql.inputs.TransactionInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        transactionMapper = new TransactionMapper(new ModelMapper());
    }

    @Test
    @DisplayName("Should map Transaction to TransactionDTO")
    void toDto() {
        Account account = new Account();
        account.setId(1L);

        Category category = new Category();
        category.setId(2L);

        Category subcategory = new Category();
        subcategory.setId(3L);

        Transaction transaction = new Transaction();
        transaction.setId(10L);
        transaction.setAmount(new BigDecimal("250.00"));
        transaction.setType(TransactionType.INFLOW);
        transaction.setDescription("Salary");
        transaction.setSource("Employer");
        transaction.setDestination("My Account");
        transaction.setTransactionDate(LocalDate.of(2025, 1, 15));
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setSubcategory(subcategory);

        TransactionDTO dto = transactionMapper.toDto(transaction);

        assertEquals(10L, dto.getId());
        assertEquals("250.00", dto.getAmount());
        assertEquals(TransactionType.INFLOW, dto.getType());
        assertEquals(1L, dto.getAccountId());
        assertEquals(2L, dto.getCategoryId());
        assertEquals(3L, dto.getSubcategoryId());
        assertEquals("2025-01-15", dto.getTransactionDate());
    }

    @Test
    @DisplayName("Should map Transaction to DTO with null category")
    void toDto_nullCategory() {
        Account account = new Account();
        account.setId(1L);

        Transaction transaction = new Transaction();
        transaction.setId(10L);
        transaction.setAmount(new BigDecimal("50.00"));
        transaction.setType(TransactionType.OUTFLOW);
        transaction.setTransactionDate(LocalDate.of(2025, 2, 1));
        transaction.setAccount(account);
        transaction.setCategory(null);
        transaction.setSubcategory(null);

        TransactionDTO dto = transactionMapper.toDto(transaction);

        assertNull(dto.getCategoryId());
        assertNull(dto.getSubcategoryId());
    }

    @Test
    @DisplayName("Should map TransactionInput to Transaction")
    void fromInput() {
        TransactionInput input = new TransactionInput();
        input.setAmount("100.00");
        input.setType(TransactionType.INFLOW);
        input.setDescription("Test");
        input.setSource("src");
        input.setDestination("dst");
        input.setTransactionDate("2025-03-01");
        input.setAccountId(1L);
        input.setCategoryId(2L);
        input.setSubcategoryId(3L);

        Transaction result = transactionMapper.fromInput(input);

        assertEquals(new BigDecimal("100.00"), result.getAmount());
        assertEquals(TransactionType.INFLOW, result.getType());
        assertEquals(1L, result.getAccount().getId());
        assertEquals(2L, result.getCategory().getId());
        assertEquals(3L, result.getSubcategory().getId());
    }

    @Test
    @DisplayName("Should map TransactionInput without categories")
    void fromInput_noCategoryIds() {
        TransactionInput input = new TransactionInput();
        input.setAmount("50.00");
        input.setType(TransactionType.OUTFLOW);
        input.setTransactionDate("2025-03-01");
        input.setAccountId(1L);

        Transaction result = transactionMapper.fromInput(input);

        assertNull(result.getCategory());
        assertNull(result.getSubcategory());
    }

    @Test
    @DisplayName("Should map list to TransactionListWithBalanceDTO")
    void toListWithBalanceDTO() {
        Account account = new Account();
        account.setId(1L);

        Transaction t = new Transaction();
        t.setId(1L);
        t.setAmount(new BigDecimal("100.00"));
        t.setType(TransactionType.INFLOW);
        t.setTransactionDate(LocalDate.of(2025, 1, 1));
        t.setAccount(account);

        TransactionListWithBalanceDTO dto = transactionMapper.toListWithBalanceDTO(List.of(t), new BigDecimal("500.00"));

        assertEquals("500.00", dto.getBalance());
        assertEquals(1, dto.getTransactions().size());
    }
}
