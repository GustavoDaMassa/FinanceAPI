package com.gustavohenrique.financeApi.application.repositories;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Should filter transactions by accountId, categoryIds and subcategoryIds")
    void findByFilter_shouldReturnMatchingTransactions() {

        User user = userRepository.save(new User(null, "Gustavo", "gustavo@test.com", "123456", null, null));
        Account account = accountRepository.save(new Account(null, "Conta 1", "Banco 1", "Corrente", BigDecimal.TEN, user, null, null));
        Category category = categoryRepository.save(new Category(null, "Alimentação", user, null, null));
        Category subcategory = categoryRepository.save(new Category(null, "Restaurantes", user, category, null));

        Transaction tx1 = new Transaction(null, new BigDecimal("100.00"), TransactionType.OUTFLOW, "Tx1", null, null,
                LocalDate.now(), category, subcategory, account);
        Transaction tx2 = new Transaction(null, new BigDecimal("200.00"), TransactionType.OUTFLOW, "Tx2", null, null,
                LocalDate.now(), category, null, account);
        Transaction tx3 = new Transaction(null, new BigDecimal("300.00"), TransactionType.OUTFLOW, "Tx3", null, null,
                LocalDate.now(), null, null, account);
        transactionRepository.saveAll(List.of(tx1, tx2, tx3));


        List<Transaction> result = transactionRepository.findByFilter(
                account.getId(),
                List.of(category.getId()),
                List.of(subcategory.getId())
        );


        assertEquals(1, result.size());
        assertEquals("Tx1", result.getFirst().getDescription());
    }

    @Test
    @DisplayName("Should return all transactions when all filters are null")
    void findByFilter_allParamsNull_shouldReturnAll() {
        User user = userRepository.save(new User(null, "Gustavo", "gustavo@test.com", "123456", null, null));
        Account account = accountRepository.save(new Account(null, "Conta 1", "Banco 1", "Corrente", BigDecimal.TEN, user, null, null));
        Transaction tx1 = new Transaction(null, new BigDecimal("50.00"), TransactionType.INFLOW, "Tx1", null, null,
                LocalDate.now(), null, null, account);
        Transaction tx2 = new Transaction(null, new BigDecimal("70.00"), TransactionType.OUTFLOW, "Tx2", null, null,
                LocalDate.now(), null, null, account);
        transactionRepository.saveAll(List.of(tx1, tx2));

        List<Transaction> result = transactionRepository.findByFilter(null, null, null);

        assertEquals(2, result.size());
    }
}
