package com.gustavohenrique.financeApi.application.repositories;

import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount_Id(Long accountId);

    List<Transaction> findByAccount_User_Id(Long userId);

    List<Transaction> findByAccountIdAndTransactionDateBetween(Long accountId, LocalDate start, LocalDate end);

    List<Transaction> findByAccountIdAndType(Long accountId, TransactionType type);

    List<Transaction> findByAccountIdAndCategoryIsNull(Long accountId);

    @Query("""
        SELECT t FROM Transaction t
        WHERE (:accountId IS NULL OR t.account.id = :accountId)
          AND (:categoryIds IS NULL OR t.category.id IN :categoryIds)
          AND (:subcategoryIds IS NULL OR t.subcategory.id IN :subcategoryIds)
    """)
    List<Transaction> findByFilter(
            @Param("accountId") Long accountId,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("subcategoryIds") List<Long> subcategoryIds
    );

}
