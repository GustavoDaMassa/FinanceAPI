package com.gustavohenrique.financeApi.exception;

import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;

public interface GraphQLErrorCreator {
    GraphQLError createError(DataFetchingEnvironment env);
}
