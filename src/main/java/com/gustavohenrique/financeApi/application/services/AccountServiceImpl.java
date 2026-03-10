package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.AccountBalanceService;
import com.gustavohenrique.financeApi.application.interfaces.AccountService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.FinancialIntegrationRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import java.util.Optional;
import com.gustavohenrique.financeApi.exception.AccountNotFoundException;
import com.gustavohenrique.financeApi.exception.IntegrationNotFoundException;
import com.gustavohenrique.financeApi.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final FinancialIntegrationRepository integrationRepository;
    private final AccountBalanceService accountBalanceService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              UserRepository userRepository,
                              FinancialIntegrationRepository integrationRepository,
                              AccountBalanceService accountBalanceService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.integrationRepository = integrationRepository;
        this.accountBalanceService = accountBalanceService;
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
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
    public Account linkAccount(Long integrationId, Account account, User authenticatedUser) {
        FinancialIntegration integration = findIntegrationById(integrationId);
        if (!integration.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccessDeniedException("Integration does not belong to the authenticated user.");
        }
        account.setIntegration(integration);
        return create(account);
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

    @Override
    public Optional<Account> findByPluggyAccountIdAndUser(String pluggyAccountId, User user) {
        return accountRepository.findByPluggyAccountIdAndUser(pluggyAccountId, user);
    }

    @Override
    public void recalculateBalance(Long accountId) {
        accountBalanceService.recalculateBalance(accountId);
    }
}
