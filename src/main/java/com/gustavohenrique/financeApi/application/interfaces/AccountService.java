package com.gustavohenrique.financeApi.application.interfaces;

import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Account findById(Long id);

    List<Account> findByUserId(Long userId);

    FinancialIntegration findIntegrationById(Long integrationId);

    Account create(Account account);

    Account linkAccount(Long integrationId, Account account, User authenticatedUser);

    Account update(Long id, Account account);

    Account delete(Long id);

    void recalculateBalance(Long accountId);

    Optional<Account> findByPluggyAccountIdAndUser(String pluggyAccountId, User user);
}
