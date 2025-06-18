package com.gustavohenrique.financeApi.application.interfaces;

import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface AccountService {


    Account findById(Long id);

    List<Account> findByUserId(Long userId);

    User findUserById(@NotBlank Long userId);

    FinancialIntegration findIntegrationById(Long integrationId);

    Account create(Account account);

    Account update(Long id, Account account);

    Account delete(Long id);
}
