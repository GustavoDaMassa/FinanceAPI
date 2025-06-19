package com.gustavohenrique.financeApi.application.repositories;

import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialIntegrationRepository extends JpaRepository<FinancialIntegration,Long> {
    List<FinancialIntegration> findByUser(User user);
}
