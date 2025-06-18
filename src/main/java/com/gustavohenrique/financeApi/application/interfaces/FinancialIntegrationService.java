package com.gustavohenrique.financeApi.application.interfaces;

import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface FinancialIntegrationService {
    FinancialIntegration findById(Long id);

    List<FinancialIntegration> findByUserId(Long userId);

    User findUserById(@NotNull Long userId);

    FinancialIntegration create(FinancialIntegration financialIntegration);

    FinancialIntegration update(Long id, FinancialIntegration financialIntegration);

    FinancialIntegration delete(Long id);
}
