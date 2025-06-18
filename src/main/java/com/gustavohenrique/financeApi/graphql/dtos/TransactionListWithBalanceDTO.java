package com.gustavohenrique.financeApi.graphql.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TransactionListWithBalanceDTO {
    private List<TransactionDTO> transactions;
    private String balance;
}
