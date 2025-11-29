package com.gustavohenrique.financeApi.graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPageDTO {
    private List<TransactionDTO> transactions;
    private PageInfo pageInfo;
    private BigDecimal balance;
}
