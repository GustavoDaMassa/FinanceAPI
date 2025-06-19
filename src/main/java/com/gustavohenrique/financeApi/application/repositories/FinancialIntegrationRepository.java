package com.gustavohenrique.financeApi.application.repositories;

import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinancialIntegrationRepository extends JpaRepository<FinancialIntegration,Long> {
}
