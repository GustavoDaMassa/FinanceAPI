package com.gustavohenrique.financeApi.application.interfaces;

import com.gustavohenrique.financeApi.application.wrappers.TransactionPageResult;
import com.gustavohenrique.financeApi.application.wrappers.TransactionQueryResult;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface TransactionService {

    TransactionQueryResult listByUserId(Long userId);

    TransactionQueryResult listByAccount(Long accountId);

    TransactionQueryResult listByPeriod(Long accountId, @NotBlank String startDate, String endDate);

    TransactionQueryResult listByType(Long accountId, String type);

    TransactionQueryResult listByFilter(Long accountId, List<Long> categoryIds);

    List<Transaction> listUncategorized(Long accountId);

    TransactionPageResult listByAccountPaginated(Long accountId, int page, int size);

    TransactionPageResult listByPeriodPaginated(Long accountId, String startDate, String endDate, int page, int size);

    TransactionPageResult listByTypePaginated(Long accountId, String type, int page, int size);

    // User-scoped variants (accountId optional — fallback to authenticated user)
    TransactionQueryResult listByPeriodForUser(Long userId, String startDate, String endDate);

    TransactionQueryResult listByTypeForUser(Long userId, String type);

    TransactionQueryResult listByFilterForUser(Long userId, List<Long> categoryIds);

    List<Transaction> listUncategorizedForUser(Long userId);

    TransactionPageResult listByUserPaginated(Long userId, int page, int size);

    TransactionPageResult listByPeriodPaginatedForUser(Long userId, String startDate, String endDate, int page, int size);

    TransactionPageResult listByTypePaginatedForUser(Long userId, String type, int page, int size);

    Transaction findById(Long id);

    Transaction create(Transaction transaction);

    Transaction update(Long id, Transaction transaction);

    Transaction categorize(Long id, Long categoryId);

    Transaction delete(Long id);
}
