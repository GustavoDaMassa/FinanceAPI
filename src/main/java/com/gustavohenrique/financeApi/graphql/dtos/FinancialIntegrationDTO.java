package com.gustavohenrique.financeApi.graphql.dtos;

import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import com.gustavohenrique.financeApi.domain.models.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialIntegrationDTO {

    private Long id;
    private AggregatorType aggregator;
    private String linkId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private Long userId;

    private List<Account> accounts;
}
