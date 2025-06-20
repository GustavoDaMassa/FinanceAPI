package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.BalanceCalculatorService;
import com.gustavohenrique.financeApi.application.interfaces.CategoryService;
import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.TransactionRepository;
import com.gustavohenrique.financeApi.application.wrappers.TransactionQueryResult;
import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final BalanceCalculatorService balanceCalculatorService;
    private final CategoryService categoryService;

    @Override
    public TransactionQueryResult listByUserId(Long userId) {
        List<Transaction> transactions = transactionRepository.findByAccount_User_Id(userId);
        BigDecimal balance = balanceCalculatorService.calculate(transactions);
        return new TransactionQueryResult(transactions, balance);
    }

    @Override
    public TransactionQueryResult listByAccount(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccount_Id(accountId);
        BigDecimal balance = balanceCalculatorService.calculate(transactions);
        return new TransactionQueryResult(transactions, balance);
    }

    @Override
    public TransactionQueryResult listByPeriod(Long accountId, String startDate, String endDate) {
        List<Transaction> transactions = transactionRepository.findByAccountIdAndTransactionDateBetween(
                accountId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
        BigDecimal balance = balanceCalculatorService.calculate(transactions);
        return new TransactionQueryResult(transactions, balance);
    }

    @Override
    public TransactionQueryResult listByType(Long accountId, String type) {
        TransactionType transactionType = TransactionType.valueOf(type);
        List<Transaction> transactions = transactionRepository.findByAccountIdAndType(accountId, transactionType);
        BigDecimal balance = balanceCalculatorService.calculate(transactions);
        return new TransactionQueryResult(transactions, balance);
    }

    @Override
    public TransactionQueryResult listByFilter(Long accountId, List<Long> categoryIds, List<Long> subcategoryIds) {
        List<Transaction> transactions = transactionRepository.findByFilter(accountId, categoryIds, subcategoryIds);
        BigDecimal balance = balanceCalculatorService.calculate(transactions);
        return new TransactionQueryResult(transactions, balance);
    }

    @Override
    public List<Transaction> listUncategorized(Long accountId) {
        return transactionRepository.findByAccountIdAndCategoryIsNull(accountId);
    }

    @Override
    public Transaction create(Transaction transaction) {

        transactionRepository.save(transaction);
        updateAccountBalance(transaction.getAccount().getId());
        return transaction;
    }

    @Override
    public Transaction update(Long id, Transaction transaction) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        existing.setAmount(transaction.getAmount());
        existing.setType(transaction.getType());
        existing.setDescription(transaction.getDescription());
        existing.setSource(transaction.getSource());
        existing.setDestination(transaction.getDestination());
        existing.setTransactionDate(transaction.getTransactionDate());
        existing.setAccount(transaction.getAccount());
        existing.setCategory(transaction.getCategory());
        existing.setSubcategory(transaction.getSubcategory());

        transactionRepository.save(existing);
        updateAccountBalance(existing.getAccount().getId());
        return existing;
    }

    @Override
    public Transaction categorize(Long id, Long categoryId, Long subcategoryId) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        Category category = categoryId != null ? categoryService.findById(categoryId) : null;
        Category subcategory = subcategoryId != null ? categoryService.findById(subcategoryId) : null;

        transaction.setCategory(category);
        transaction.setSubcategory(subcategory);

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction delete(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));
        transactionRepository.delete(transaction);
        updateAccountBalance(transaction.getAccount().getId());
        return transaction;
    }

    private void updateAccountBalance(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccount_Id(accountId);
        BigDecimal newBalance = balanceCalculatorService.calculate(transactions);

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        account.setBalance(newBalance);
        accountRepository.save(account);
    }

}
