package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.AccountService;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Override
    public Account findById(Long id) {
        return null;
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        return List.of();
    }

    @Override
    public User findUserById(Long userId) {
        return null;
    }

    @Override
    public FinancialIntegration findIntegrationById(Long integrationId) {
        return null;
    }

    @Override
    public Account create(Account account) {
        return null;
    }

    @Override
    public Account update(Long id, Account account) {
        return null;
    }

    @Override
    public Account delete(Long id) {
        return null;
    }
}
