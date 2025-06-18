package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancialIntegrationServiceImpl implements FinancialIntegrationService {
    @Override
    public FinancialIntegration findById(Long id) {
        return null;
    }

    @Override
    public List<FinancialIntegration> findByUserId(Long userId) {
        return List.of();
    }

    @Override
    public User findUserById(Long userId) {
        return null;
    }

    @Override
    public FinancialIntegration create(FinancialIntegration financialIntegration) {
        return null;
    }

    @Override
    public FinancialIntegration update(Long id, FinancialIntegration financialIntegration) {
        return null;
    }

    @Override
    public FinancialIntegration delete(Long id) {
        return null;
    }
}
