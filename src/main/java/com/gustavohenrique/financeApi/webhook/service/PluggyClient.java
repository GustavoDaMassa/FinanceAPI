package com.gustavohenrique.financeApi.webhook.service;

import com.gustavohenrique.financeApi.graphql.dtos.PluggyAccountDTO;
import com.gustavohenrique.financeApi.webhook.dataTransfer.ListTransactionsResponse;
import com.gustavohenrique.financeApi.webhook.dataTransfer.TransactionResponse;

import java.util.List;

public interface PluggyClient {

    ListTransactionsResponse fetchTransaction(String linkTransactions);

    List<TransactionResponse> fetchTransactionsByAccount(String pluggyAccountId);

    List<PluggyAccountDTO> fetchAccounts(String itemId);

    String createConnectToken();

    String createConnectToken(String itemId);
}