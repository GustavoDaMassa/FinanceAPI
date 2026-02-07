package com.gustavohenrique.financeApi.application.wrappers;

import com.gustavohenrique.financeApi.domain.models.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionPageResult {
    private Page<Transaction> page;
    private BigDecimal balance;
}
