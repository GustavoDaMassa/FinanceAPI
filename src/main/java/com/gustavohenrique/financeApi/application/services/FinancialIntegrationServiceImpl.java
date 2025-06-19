package com.gustavohenrique.financeApi.application.services;

import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.application.repositories.FinancialIntegrationRepository;
import com.gustavohenrique.financeApi.application.repositories.UserRepository;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialIntegrationServiceImpl implements FinancialIntegrationService {

    private final FinancialIntegrationRepository integrationRepository;
    private final UserRepository userRepository;

    @Override
    public FinancialIntegration findById(Long id) {
        return integrationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Integration not found with ID: " + id));
    }

    @Override
    public List<FinancialIntegration> findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        return integrationRepository.findByUser(user);
    }

    @Override
    public FinancialIntegration create(FinancialIntegration financialIntegration) {
        financialIntegration.setCreatedAt(LocalDateTime.now());
        financialIntegration.setExpiresAt(LocalDateTime.now().plusMonths(6));
        return integrationRepository.save(financialIntegration);
    }

    @Override
    public FinancialIntegration update(Long id, FinancialIntegration updatedIntegration) {
        FinancialIntegration existing = findById(id);

        existing.setAggregator(updatedIntegration.getAggregator());
        existing.setLinkId(updatedIntegration.getLinkId());
        existing.setStatus(updatedIntegration.getStatus());
        existing.setExpiresAt(updatedIntegration.getExpiresAt());
        existing.setUser(updatedIntegration.getUser());

        return integrationRepository.save(existing);
    }

    @Override
    public FinancialIntegration delete(Long id) {
        FinancialIntegration existing = findById(id);
        integrationRepository.delete(existing);
        return existing;
    }
}
