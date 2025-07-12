package com.gustavohenrique.financeApi.webhook.consumer;

import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.User;

public interface SetUpWebhook {
    Long UserWebhookID (User user);

    Account AccountWebhookId(Account account);
}
