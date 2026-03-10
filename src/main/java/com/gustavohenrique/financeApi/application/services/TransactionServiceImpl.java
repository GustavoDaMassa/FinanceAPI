package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.AccountBalanceService;
import com.gustavohenrique.financeApi.application.interfaces.BalanceCalculatorService;
import com.gustavohenrique.financeApi.application.interfaces.CategoryService;
import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.TransactionRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.application.wrappers.TransactionPageResult;
import com.gustavohenrique.financeApi.application.wrappers.TransactionQueryResult;
import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.exception.AccountNotFoundException;
import com.gustavohenrique.financeApi.exception.TransactionNotFoundException;
import com.gustavohenrique.financeApi.exception.UserIDNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BalanceCalculatorService balanceCalculatorService;
    private final CategoryService categoryService;
    private final AccountBalanceService accountBalanceService;

    @Override
    public TransactionQueryResult listByUserId(Long userId) {
        requireUserExists(userId);
        return buildResult(transactionRepository.findByAccount_User_Id(userId));
    }

    @Override
    public TransactionQueryResult listByAccount(Long accountId) {
        requireAccountExists(accountId);
        return buildResult(transactionRepository.findByAccount_Id(accountId));
    }

    @Override
    public TransactionQueryResult listByPeriod(Long accountId, String startDate, String endDate) {
        requireAccountExists(accountId);
        return buildResult(transactionRepository.findByAccountIdAndTransactionDateBetween(
                accountId, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }

    @Override
    public TransactionQueryResult listByType(Long accountId, String type) {
        requireAccountExists(accountId);
        return buildResult(transactionRepository.findByAccountIdAndType(accountId, TransactionType.valueOf(type)));
    }

    @Override
    public TransactionQueryResult listByFilter(Long accountId, List<Long> categoryIds) {
        requireAccountExists(accountId);
        return buildResult(transactionRepository.findByFilter(accountId, categoryIds));
    }

    @Override
    public List<Transaction> listUncategorized(Long accountId) {
        requireAccountExists(accountId);
        return transactionRepository.findByAccountIdAndCategoryIsNull(accountId);
    }

    @Override
    public TransactionPageResult listByAccountPaginated(Long accountId, int page, int size) {
        requireAccountExists(accountId);
        Pageable pageable = pageRequest(page, size);
        return buildPageResult(transactionRepository.findByAccount_Id(accountId, pageable),
                transactionRepository.findByAccount_Id(accountId));
    }

    @Override
    public TransactionPageResult listByPeriodPaginated(Long accountId, String startDate, String endDate, int page, int size) {
        requireAccountExists(accountId);
        Pageable pageable = pageRequest(page, size);
        return buildPageResult(
                transactionRepository.findByAccountIdAndTransactionDateBetween(accountId, LocalDate.parse(startDate), LocalDate.parse(endDate), pageable),
                transactionRepository.findByAccountIdAndTransactionDateBetween(accountId, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }

    @Override
    public TransactionPageResult listByTypePaginated(Long accountId, String type, int page, int size) {
        requireAccountExists(accountId);
        TransactionType transactionType = TransactionType.valueOf(type);
        Pageable pageable = pageRequest(page, size);
        return buildPageResult(transactionRepository.findByAccountIdAndType(accountId, transactionType, pageable),
                transactionRepository.findByAccountIdAndType(accountId, transactionType));
    }

    // ── User-scoped variants ──────────────────────────────────────────────────

    @Override
    public TransactionQueryResult listByPeriodForUser(Long userId, String startDate, String endDate) {
        requireUserExists(userId);
        return buildResult(transactionRepository.findByAccount_User_IdAndTransactionDateBetween(
                userId, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }

    @Override
    public TransactionQueryResult listByTypeForUser(Long userId, String type) {
        requireUserExists(userId);
        return buildResult(transactionRepository.findByAccount_User_IdAndType(userId, TransactionType.valueOf(type)));
    }

    @Override
    public TransactionQueryResult listByFilterForUser(Long userId, List<Long> categoryIds) {
        requireUserExists(userId);
        return buildResult(transactionRepository.findByFilterForUser(userId, categoryIds));
    }

    @Override
    public List<Transaction> listUncategorizedForUser(Long userId) {
        requireUserExists(userId);
        return transactionRepository.findByAccount_User_IdAndCategoryIsNull(userId);
    }

    @Override
    public TransactionPageResult listByUserPaginated(Long userId, int page, int size) {
        requireUserExists(userId);
        Pageable pageable = pageRequest(page, size);
        return buildPageResult(transactionRepository.findByAccount_User_Id(userId, pageable),
                transactionRepository.findByAccount_User_Id(userId));
    }

    @Override
    public TransactionPageResult listByPeriodPaginatedForUser(Long userId, String startDate, String endDate, int page, int size) {
        requireUserExists(userId);
        Pageable pageable = pageRequest(page, size);
        return buildPageResult(
                transactionRepository.findByAccount_User_IdAndTransactionDateBetween(userId, LocalDate.parse(startDate), LocalDate.parse(endDate), pageable),
                transactionRepository.findByAccount_User_IdAndTransactionDateBetween(userId, LocalDate.parse(startDate), LocalDate.parse(endDate)));
    }

    @Override
    public TransactionPageResult listByTypePaginatedForUser(Long userId, String type, int page, int size) {
        requireUserExists(userId);
        TransactionType transactionType = TransactionType.valueOf(type);
        Pageable pageable = pageRequest(page, size);
        return buildPageResult(transactionRepository.findByAccount_User_IdAndType(userId, transactionType, pageable),
                transactionRepository.findByAccount_User_IdAndType(userId, transactionType));
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Override
    public Transaction create(Transaction transaction) {
        requireAccountExists(transaction.getAccount().getId());
        transactionRepository.save(transaction);
        accountBalanceService.recalculateBalance(transaction.getAccount().getId());
        return transaction;
    }

    @Override
    public Transaction update(Long id, Transaction transaction) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        existing.setAmount(transaction.getAmount());
        existing.setType(transaction.getType());
        existing.setDescription(transaction.getDescription());
        existing.setSource(transaction.getSource());
        existing.setDestination(transaction.getDestination());
        existing.setTransactionDate(transaction.getTransactionDate());
        existing.setAccount(transaction.getAccount());
        existing.setCategory(transaction.getCategory());

        transactionRepository.save(existing);
        accountBalanceService.recalculateBalance(existing.getAccount().getId());
        return existing;
    }

    @Override
    public Transaction categorize(Long id, Long categoryId) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        Category category = categoryId != null ? categoryService.findById(categoryId) : null;

        transaction.setCategory(category);

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction delete(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        transactionRepository.delete(transaction);
        accountBalanceService.recalculateBalance(transaction.getAccount().getId());
        return transaction;
    }

    @Override
    public boolean existsByExternalId(String externalId) {
        return transactionRepository.existsByExternalId(externalId);
    }

    private void requireAccountExists(Long accountId) {
        if (!accountRepository.existsById(accountId)) throw new AccountNotFoundException(accountId);
    }

    private void requireUserExists(Long userId) {
        if (!userRepository.existsById(userId)) throw new UserIDNotFoundException(userId);
    }

    private Pageable pageRequest(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
    }

    private TransactionQueryResult buildResult(List<Transaction> transactions) {
        return new TransactionQueryResult(transactions, balanceCalculatorService.calculate(transactions));
    }

    private TransactionPageResult buildPageResult(Page<Transaction> page, List<Transaction> allForBalance) {
        return new TransactionPageResult(page, balanceCalculatorService.calculate(allForBalance));
    }

}
