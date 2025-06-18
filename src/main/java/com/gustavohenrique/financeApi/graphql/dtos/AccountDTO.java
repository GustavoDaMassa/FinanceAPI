package com.gustavohenrique.financeApi.graphql.dtos;

import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;
    private String institution;
    private String type;
    private String accountName;
    private String balance;
    private Long userId;
    private Long IntegrationId;

}
