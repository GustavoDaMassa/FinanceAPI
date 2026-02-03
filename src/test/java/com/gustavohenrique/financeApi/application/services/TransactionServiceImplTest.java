package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.BalanceCalculatorService;
import com.gustavohenrique.financeApi.application.interfaces.CategoryService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.TransactionRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.application.wrappers.TransactionPageResult;
import com.gustavohenrique.financeApi.application.wrappers.TransactionQueryResult;
import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private AccountRepository accountRepository;
    @Mock private UserRepository userRepository;
    @Mock private BalanceCalculatorService balanceCalculatorService;
    @Mock private CategoryService categoryService;

    @InjectMocks private TransactionServiceImpl transactionService;

    private Transaction transaction;
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(1L, "Main", "Bank", "Checking", BigDecimal.ZERO, null, null, null, null);
        transaction = new Transaction(1L, new BigDecimal("100.00"), TransactionType.INFLOW, "Salary",
                "Employer", "Me", LocalDate.now(), null, null, account);
    }

    @Test
    @DisplayName("Should return transactions and balance by user ID")
    void listByUserId() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByAccount_User_Id(1L)).thenReturn(List.of(transaction));
        when(balanceCalculatorService.calculate(List.of(transaction))).thenReturn(new BigDecimal("100.00"));

        TransactionQueryResult result = transactionService.listByUserId(1L);

        assertEquals(1, result.getTransactions().size());
        assertEquals(new BigDecimal("100.00"), result.getBalance());
    }

    @Test
    @DisplayName("Should return transactions and balance by account ID")
    void listByAccount() {
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByAccount_Id(1L)).thenReturn(List.of(transaction));
        when(balanceCalculatorService.calculate(List.of(transaction))).thenReturn(new BigDecimal("100.00"));

        TransactionQueryResult result = transactionService.listByAccount(1L);

        assertEquals(1, result.getTransactions().size());
    }

    @Test
    @DisplayName("Should return transactions and balance by date period")
    void listByPeriod() {
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByAccountIdAndTransactionDateBetween(eq(1L), any(), any())).thenReturn(List.of(transaction));
        when(balanceCalculatorService.calculate(List.of(transaction))).thenReturn(new BigDecimal("100.00"));

        TransactionQueryResult result = transactionService.listByPeriod(1L, "2024-01-01", "2024-12-31");

        assertEquals(1, result.getTransactions().size());
    }

    @Test
    @DisplayName("Should return transactions and balance by type")
    void listByType() {
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByAccountIdAndType(1L, TransactionType.INFLOW)).thenReturn(List.of(transaction));
        when(balanceCalculatorService.calculate(List.of(transaction))).thenReturn(new BigDecimal("100.00"));

        TransactionQueryResult result = transactionService.listByType(1L, "INFLOW");

        assertEquals(1, result.getTransactions().size());
    }

    @Test
    @DisplayName("Should return transactions and balance by custom filters")
    void listByFilter() {
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByFilter(1L, List.of(1L), List.of())).thenReturn(List.of(transaction));
        when(balanceCalculatorService.calculate(List.of(transaction))).thenReturn(new BigDecimal("100.00"));

        TransactionQueryResult result = transactionService.listByFilter(1L, List.of(1L), List.of());

        assertEquals(1, result.getTransactions().size());
    }

    @Test
    @DisplayName("Should return uncategorized transactions")
    void listUncategorized() {
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByAccountIdAndCategoryIsNull(1L)).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.listUncategorized(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should create transaction and update account balance")
    void create() {
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionRepository.findByAccount_Id(1L)).thenReturn(List.of(transaction));
        when(balanceCalculatorService.calculate(List.of(transaction))).thenReturn(new BigDecimal("100.00"));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Transaction result = transactionService.create(transaction);

        assertEquals(transaction, result);
        verify(transactionRepository).save(transaction);
    }

    @Test
    @DisplayName("Should update transaction if it exists")
    void update() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any())).thenReturn(transaction);
        when(transactionRepository.findByAccount_Id(1L)).thenReturn(List.of(transaction));
        when(balanceCalculatorService.calculate(List.of(transaction))).thenReturn(new BigDecimal("100.00"));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Transaction result = transactionService.update(1L, transaction);

        assertEquals(transaction, result);
        verify(transactionRepository).save(transaction);
    }

    @Test
    @DisplayName("Should categorize transaction by category and subcategory IDs")
    void categorize() {
        Category category = new Category(1L, "Food", null, null, null);
        Category subcategory = new Category(2L, "Groceries", null, null, null);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(categoryService.findById(1L)).thenReturn(category);
        when(categoryService.findById(2L)).thenReturn(subcategory);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.categorize(1L, 1L, 2L);

        assertEquals(category, result.getCategory());
        assertEquals(subcategory, result.getSubcategory());
    }

    @Test
    @DisplayName("Should delete transaction and update account balance")
    void delete() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.findByAccount_Id(1L)).thenReturn(List.of());
        when(balanceCalculatorService.calculate(List.of())).thenReturn(BigDecimal.ZERO);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Transaction deleted = transactionService.delete(1L);

        assertEquals(transaction, deleted);
        verify(transactionRepository).delete(transaction);
    }

    @Test
    @DisplayName("Should return paginated transactions by account ID")
    void listByAccountPaginated() {
        Page<Transaction> page = new PageImpl<>(List.of(transaction));
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByAccount_Id(eq(1L), any(Pageable.class))).thenReturn(page);
        when(transactionRepository.findByAccount_Id(1L)).thenReturn(List.of(transaction));
        when(balanceCalculatorService.calculate(List.of(transaction))).thenReturn(new BigDecimal("100.00"));

        TransactionPageResult result = transactionService.listByAccountPaginated(1L, 0, 10);

        assertEquals(1, result.getPage().getContent().size());
        assertEquals(new BigDecimal("100.00"), result.getBalance());
    }

    @Test
    @DisplayName("Should return paginated transactions by period")
    void listByPeriodPaginated() {
        Page<Transaction> page = new PageImpl<>(List.of(transaction));
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByAccountIdAndTransactionDateBetween(eq(1L), any(), any(), any(Pageable.class))).thenReturn(page);
        when(transactionRepository.findByAccountIdAndTransactionDateBetween(eq(1L), any(), any())).thenReturn(List.of(transaction));
        when(balanceCalculatorService.calculate(List.of(transaction))).thenReturn(new BigDecimal("100.00"));

        TransactionPageResult result = transactionService.listByPeriodPaginated(1L, "2024-01-01", "2024-12-31", 0, 10);

        assertEquals(1, result.getPage().getContent().size());
        assertEquals(new BigDecimal("100.00"), result.getBalance());
    }

    @Test
    @DisplayName("Should return paginated transactions by type")
    void listByTypePaginated() {
        Page<Transaction> page = new PageImpl<>(List.of(transaction));
        when(accountRepository.existsById(1L)).thenReturn(true);
        when(transactionRepository.findByAccountIdAndType(eq(1L), eq(TransactionType.INFLOW), any(Pageable.class))).thenReturn(page);
        when(transactionRepository.findByAccountIdAndType(1L, TransactionType.INFLOW)).thenReturn(List.of(transaction));
        when(balanceCalculatorService.calculate(List.of(transaction))).thenReturn(new BigDecimal("100.00"));

        TransactionPageResult result = transactionService.listByTypePaginated(1L, "INFLOW", 0, 10);

        assertEquals(1, result.getPage().getContent().size());
        assertEquals(new BigDecimal("100.00"), result.getBalance());
    }
}
