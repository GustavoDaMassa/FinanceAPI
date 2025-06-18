package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.FinancialIntegrationDTO;
import com.gustavohenrique.financeApi.graphql.inputs.FinancialIntegrationInput;
import com.gustavohenrique.financeApi.graphql.mappers.FinancialIntegrationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FinancialIntegrationResolver {

    private final FinancialIntegrationService integrationService;
    private final FinancialIntegrationMapper mapper;

    // ===== Queries =====

    @QueryMapping
    public FinancialIntegrationDTO findFinancialIntegrationById(@Argument Long id) {
        FinancialIntegration integration = integrationService.findById(id);
        return mapper.toDto(integration);
    }

    @QueryMapping
    public List<FinancialIntegrationDTO> listFinancialIntegrationsByUser(@Argument Long userId) {
        return integrationService.findByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    // ===== Mutations =====

    @MutationMapping
    public FinancialIntegrationDTO createFinancialIntegration(@Argument FinancialIntegrationInput input) {
        User user = integrationService.findUserById(input.getUserId());
        FinancialIntegration created = integrationService.create(mapper.fromInput(input, user));
        return mapper.toDto(created);
    }

    @MutationMapping
    public FinancialIntegrationDTO updateFinancialIntegration(@Argument Long id, @Argument FinancialIntegrationInput input) {
        User user = integrationService.findUserById(input.getUserId());
        FinancialIntegration updated = integrationService.update(id, mapper.fromInput(input, user));
        return mapper.toDto(updated);
    }

    @MutationMapping
    public FinancialIntegrationDTO deleteFinancialIntegration(@Argument Long id) {
        FinancialIntegration deleted = integrationService.delete(id);
        return mapper.toDto(deleted);
    }
}
