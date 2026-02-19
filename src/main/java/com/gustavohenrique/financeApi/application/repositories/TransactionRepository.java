package com.gustavohenrique.financeApi.application.repositories;

import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    """)
    List<Transaction> findByFilter(
            @Param("accountId") Long accountId,
            @Param("categoryIds") List<Long> categoryIds
    );

    Page<Transaction> findByAccount_Id(Long accountId, Pageable pageable);

    Page<Transaction> findByAccountIdAndTransactionDateBetween(Long accountId, LocalDate start, LocalDate end, Pageable pageable);

    Page<Transaction> findByAccountIdAndType(Long accountId, TransactionType type, Pageable pageable);

    // User-scoped variants (accountId optional — fallback to authenticated user)
    List<Transaction> findByAccount_User_IdAndTransactionDateBetween(Long userId, LocalDate start, LocalDate end);

    List<Transaction> findByAccount_User_IdAndType(Long userId, TransactionType type);

    List<Transaction> findByAccount_User_IdAndCategoryIsNull(Long userId);

    @Query("""
        SELECT t FROM Transaction t
        WHERE t.account.user.id = :userId
          AND (:categoryIds IS NULL OR t.category.id IN :categoryIds)
    """)
    List<Transaction> findByFilterForUser(
            @Param("userId") Long userId,
            @Param("categoryIds") List<Long> categoryIds
    );

    Page<Transaction> findByAccount_User_Id(Long userId, Pageable pageable);

    Page<Transaction> findByAccount_User_IdAndTransactionDateBetween(Long userId, LocalDate start, LocalDate end, Pageable pageable);

    Page<Transaction> findByAccount_User_IdAndType(Long userId, TransactionType type, Pageable pageable);

    boolean existsByExternalId(String externalId);

}
