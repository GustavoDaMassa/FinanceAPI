package com.gustavohenrique.financeApi.webhook.dataTransfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gustavohenrique.financeApi.graphql.dtos.PluggyAccountDTO;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ListAccountsResponse {
    private List<PluggyAccountDTO> results;
}
