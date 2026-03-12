package com.gustavohenrique.financeApi.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.ErrorType;

public abstract class BadRequestException extends RuntimeException implements GraphQLErrorCreator {
    protected BadRequestException(String message) {
        super(message);
    }

    @Override
    public GraphQLError createError(DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message(getMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .build();
    }
}
