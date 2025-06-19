package com.gustavohenrique.financeApi.graphql.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TransactionListWithBalanceDTO {
    private String balance;
    private List<TransactionDTO> transactions;
}
