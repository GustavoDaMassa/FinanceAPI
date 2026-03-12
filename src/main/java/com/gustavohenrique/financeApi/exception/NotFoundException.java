package com.gustavohenrique.financeApi.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.ErrorType;

public abstract class NotFoundException extends RuntimeException implements GraphQLErrorCreator {
    protected NotFoundException(String message) {
        super(message);
    }

    @Override
    public GraphQLError createError(DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message(getMessage())
                .errorType(ErrorType.NOT_FOUND)
                .build();
    }
}
