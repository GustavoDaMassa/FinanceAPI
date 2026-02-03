package com.gustavohenrique.financeApi.graphql.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PluggyAccountDTO {
    private String id;
    private String name;
    private String type;
    private BigDecimal balance;
    private String currency;
}
