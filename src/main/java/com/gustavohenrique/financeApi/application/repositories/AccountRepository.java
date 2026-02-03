package com.gustavohenrique.financeApi.application.repositories;

import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountName(String accountName);
    Account findByAccountName(String accountName);

    Optional<Account> findByPluggyAccountIdAndUser(String pluggyAccountId, User user);
}
