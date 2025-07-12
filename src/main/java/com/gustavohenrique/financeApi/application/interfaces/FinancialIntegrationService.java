package com.gustavohenrique.financeApi.application.interfaces;

import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;

public interface FinancialIntegrationService {
    FinancialIntegration findById(Long id);

    FinancialIntegration resolveIntegrationByLinkId(String linkId, Long userId, Long accountId, AggregatorType aggregator);

    List<FinancialIntegration> findByUserId(Long userId);

    FinancialIntegration create(FinancialIntegration financialIntegration, Account account);

    FinancialIntegration update(Long id, FinancialIntegration financialIntegration);

    FinancialIntegration delete(Long id);

    List<Account> listIntegrationAccounts(Long id);

}
