package com.gustavohenrique.financeApi.application.repositories;

import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account,Long> {
    List<Account> findByUser(User user);
}
