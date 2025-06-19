package com.gustavohenrique.financeApi.application.interfaces;

import com.gustavohenrique.financeApi.application.results.TransactionQueryResult;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface TransactionService {

    TransactionQueryResult listByUserId(Long userId);

    TransactionQueryResult listByAccount(Long accountId);

    TransactionQueryResult listByPeriod(Long accountId, @NotBlank String startDate, String endDate);

    TransactionQueryResult listByType(Long accountId, String type);

    TransactionQueryResult listByFilter(Long accountId, List<Long> categoryIds, List<Long> subcategoryIds);

    List<Transaction> listUncategorized(Long accountId);

    Transaction create(Transaction transaction);

    Transaction update(Long id, Transaction transaction);

    Transaction categorize(Long id, Long categoryId, Long subcategoryId);

    Transaction delete(Long id);
}
