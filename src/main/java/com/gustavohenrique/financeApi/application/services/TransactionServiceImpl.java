package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.application.results.TransactionQueryResult;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Override
    public TransactionQueryResult listByUserId(Long userId) {
        return null;
    }

    @Override
    public TransactionQueryResult listByAccount(Long accountId) {
        return null;
    }

    @Override
    public TransactionQueryResult listByPeriod(Long accountId, String startDate, String endDate) {
        return null;
    }

    @Override
    public TransactionQueryResult listByType(Long accountId, String type) {
        return null;
    }

    @Override
    public TransactionQueryResult listByFilter(Long accountId, List<Long> categoryIds, List<Long> subcategoryIds) {
        return null;
    }

    @Override
    public List<Transaction> listUncategorized(Long accountId) {
        return List.of();
    }

    @Override
    public Transaction create(Transaction transaction) {
        return null;
    }

    @Override
    public Transaction update(Long id, Transaction transaction) {
        return null;
    }

    @Override
    public Transaction categorize(Long id, Long categoryId, Long subcategoryId) {
        return null;
    }

    @Override
    public Transaction delete(Long id) {
        return null;
    }
}
