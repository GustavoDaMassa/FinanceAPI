package com.gustavohenrique.financeApi.graphql.inputs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationInput {
    private Integer page = 0;
    private Integer size = 20;

    public Integer getPage() {
        return page != null && page >= 0 ? page : 0;
    }

    public Integer getSize() {
        return size != null && size > 0 && size <= 100 ? size : 20;
    }
}
