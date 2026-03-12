package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.exception.GraphQLErrorCreator;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Component
public class CustomGraphQLExceptionResolver extends DataFetcherExceptionResolverAdapter {

    @Override
    protected GraphQLError resolveToSingleError(Throwable e, DataFetchingEnvironment env) {
        if (e instanceof ConstraintViolationException) {
            return GraphqlErrorBuilder.newError(env)
                    .message(e.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();
        }
        if (e instanceof GraphQLErrorCreator creator) {
            return creator.createError(env);
        }
        return GraphqlErrorBuilder.newError(env)
                .message("Internal Server Error")
                .errorType(ErrorType.INTERNAL_ERROR)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GraphQLError handleValidation(MethodArgumentNotValidException e, DataFetchingEnvironment env) {
        return GraphqlErrorBuilder.newError(env)
                .message("Validation error: " + Objects.requireNonNull(Objects
                        .requireNonNull(e.getBindingResult()).getFieldError()).getDefaultMessage())
                .errorType(ErrorType.BAD_REQUEST)
                .build();


    }

}
