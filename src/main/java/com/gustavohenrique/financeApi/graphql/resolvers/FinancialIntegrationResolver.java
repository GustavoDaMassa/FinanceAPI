package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.FinancialIntegrationService;
import com.gustavohenrique.financeApi.domain.models.FinancialIntegration;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.AccountDTO;
import com.gustavohenrique.financeApi.graphql.dtos.FinancialIntegrationDTO;
import com.gustavohenrique.financeApi.graphql.inputs.FinancialIntegrationInput;
import com.gustavohenrique.financeApi.graphql.mappers.AccountMapper;
import com.gustavohenrique.financeApi.graphql.mappers.FinancialIntegrationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class FinancialIntegrationResolver {

    private final FinancialIntegrationService integrationService;
    private final FinancialIntegrationMapper mapper;
    private final AccountMapper accountMapper;



    @QueryMapping
    public FinancialIntegrationDTO findFinancialIntegrationById(@Argument Long id) {
        FinancialIntegration integration = integrationService.findById(id);
        return mapper.toDto(integration);
    }

    @QueryMapping
    public List<FinancialIntegrationDTO> listFinancialIntegrationsByUser(@AuthenticationPrincipal User user) {
        return integrationService.findByUserId(user.getId())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<AccountDTO> listAccountsByIntegration(@Argument Long id){
        return integrationService.listIntegrationAccounts(id).stream()
                .map(accountMapper::toDto)
                .collect(Collectors.toList());
    }



    @MutationMapping
    public FinancialIntegrationDTO createFinancialIntegration(@Argument String itemId, @AuthenticationPrincipal User user) {
        FinancialIntegration newIntegration = new FinancialIntegration();
        newIntegration.setLinkId(itemId);
        newIntegration.setUser(user);
        // Defaults can be set in the service
        FinancialIntegration created = integrationService.create(newIntegration);
        return mapper.toDto(created);
    }

    @MutationMapping
    public FinancialIntegrationDTO updateFinancialIntegration(@Argument Long id, @Argument FinancialIntegrationInput input) {
        // This might need adjustment if the input and mapper are user-dependent
        FinancialIntegration existingIntegration = integrationService.findById(id);
        User user = existingIntegration.getUser();
        FinancialIntegration updated = integrationService.update(id, mapper.fromInput(input, user));
        return mapper.toDto(updated);
    }

    @MutationMapping
    public FinancialIntegrationDTO deleteFinancialIntegration(@Argument Long id) {
        FinancialIntegration deleted = integrationService.delete(id);
        return mapper.toDto(deleted);
    }
}
