package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.AccountBalanceService;
import com.gustavohenrique.financeApi.application.interfaces.BalanceCalculatorService;
import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.TransactionRepository;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountBalanceServiceImpl implements AccountBalanceService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final BalanceCalculatorService balanceCalculatorService;

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
