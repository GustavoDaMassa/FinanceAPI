package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.AccountService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.FinancialIntegrationRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.AccountNotFoundException;
import com.gustavohenrique.financeApi.exception.IntegrationNotFoundException;
import com.gustavohenrique.financeApi.exception.UserIDNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final FinancialIntegrationRepository integrationRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              UserRepository userRepository,
                              FinancialIntegrationRepository integrationRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.integrationRepository = integrationRepository;
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserIDNotFoundException(userId));
        return accountRepository.findByUser(user);
    }

    @Override
    public FinancialIntegration findIntegrationById(Long integrationId) {
        return integrationRepository.findById(integrationId)
                .orElseThrow(() -> new IntegrationNotFoundException(integrationId));
    }

    @Override
    public Account create(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account update(Long id, Account updatedAccount) {
        Account existing = findById(id);
        existing.setAccountName(updatedAccount.getAccountName());
        existing.setInstitution(updatedAccount.getInstitution());
        existing.setDescription(updatedAccount.getDescription());
        existing.setUser(updatedAccount.getUser());
        existing.setIntegration(updatedAccount.getIntegration());
        return accountRepository.save(existing);
    }

    @Override
    public Account delete(Long id) {
        Account existing = findById(id);
        accountRepository.delete(existing);
        return existing;
    }
}
