package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.AccountService;
import com.gustavohenrique.financeApi.application.interfaces.BalanceCalculatorService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.FinancialIntegrationRepository;
import com.gustavohenrique.financeApi.application.repositories.TransactionRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.exception.AccountNotFoundException;
import com.gustavohenrique.financeApi.exception.IntegrationNotFoundException;
import com.gustavohenrique.financeApi.exception.UserIDNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final FinancialIntegrationRepository integrationRepository;
    private final TransactionRepository transactionRepository;
    private final BalanceCalculatorService balanceCalculatorService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              UserRepository userRepository,
                              FinancialIntegrationRepository integrationRepository,
                              TransactionRepository transactionRepository,
                              BalanceCalculatorService balanceCalculatorService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.integrationRepository = integrationRepository;
        this.transactionRepository = transactionRepository;
        this.balanceCalculatorService = balanceCalculatorService;
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

    @Override
    public void recalculateBalance(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccount_Id(accountId);
        BigDecimal newBalance = balanceCalculatorService.calculate(transactions);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}
