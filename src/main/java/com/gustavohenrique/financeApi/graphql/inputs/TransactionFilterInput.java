package com.gustavohenrique.financeApi.graphql.inputs;

import lombok.Data;
import java.util.List;

@Data
public class TransactionFilterInput {
    private List<Long> categoryIds;
    private List<Long> subcategoryIds;
}
