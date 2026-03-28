package com.gustavohenrique.financeApi.importer.dto;

import com.gustavohenrique.financeApi.graphql.dtos.TransactionDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ImportResultDTO {
    private int total;
    private int imported;
    private int skipped;
    private List<TransactionDTO> transactions;
}
