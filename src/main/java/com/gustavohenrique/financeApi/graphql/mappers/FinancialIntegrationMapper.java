package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.application.repositories.FinancialIntegrationRepository;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.FinancialIntegrationDTO;
import com.gustavohenrique.financeApi.graphql.inputs.FinancialIntegrationInput;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinancialIntegrationMapper {

    private final ModelMapper modelMapper;
    private final FinancialIntegrationRepository financialIntegrationRepository;

    @Autowired
    public FinancialIntegrationMapper(ModelMapper modelMapper, FinancialIntegrationRepository financialIntegrationRepository) {
        this.modelMapper = modelMapper;
        this.financialIntegrationRepository = financialIntegrationRepository;
    }

    public FinancialIntegrationDTO toDto(FinancialIntegration integration) {
        FinancialIntegrationDTO dto = modelMapper.map(integration, FinancialIntegrationDTO.class);
        dto.setUserId(integration.getUser().getId());
        return dto;
    }

    public FinancialIntegration fromInput(FinancialIntegrationInput input, User user) {
        FinancialIntegration integration = financialIntegrationRepository.findByLinkId(input.getLinkId())
                .orElse(new FinancialIntegration());

        integration.setAggregator(input.getAggregator());
        integration.setLinkId(input.getLinkId());
        integration.setUser(user);
        return integration;
    }
}
