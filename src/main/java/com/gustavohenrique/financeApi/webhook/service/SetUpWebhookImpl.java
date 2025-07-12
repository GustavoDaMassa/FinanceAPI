package com.gustavohenrique.financeApi.webhook.service;

import com.gustavohenrique.financeApi.application.repositories.AccountRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetUpWebhookImpl implements SetUpWebhook {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public SetUpWebhookImpl(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }


    @Override
    public Long UserWebhookID(User user) {
        if(userRepository.existsByEmail(user.getEmail())) return userRepository.findByEmail(user.getEmail()).orElseThrow().getId();
        else return userRepository.save(user).getId();
    }

    @Override
    public Account AccountWebhookId(Account account) {
        if(accountRepository.existsByAccountName(account.getAccountName()))
            return accountRepository.findByAccountName(account.getAccountName());
        else return accountRepository.save(account);
    }
}
