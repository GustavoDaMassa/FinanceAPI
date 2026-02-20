package com.gustavohenrique.financeApi.graphql.inputs;

import lombok.Data;

@Data
public class LinkAccountInput {
    private Long integrationId;
    private String pluggyAccountId;
    private String name;
    private String institution;
    private String description;
}
