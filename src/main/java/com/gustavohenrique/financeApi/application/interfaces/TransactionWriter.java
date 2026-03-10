package com.gustavohenrique.financeApi.application.interfaces;

import com.gustavohenrique.financeApi.domain.models.Transaction;

public interface TransactionWriter {

    Transaction create(Transaction transaction);

    boolean existsByExternalId(String externalId);
}
