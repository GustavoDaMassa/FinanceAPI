package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.exception.EmailAlreadyExistException;
import com.gustavohenrique.financeApi.exception.InvalidTransactionTypeException;
import com.gustavohenrique.financeApi.exception.NotFoundException;
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
        return switch (e) {
            case NotFoundException notFoundException -> GraphqlErrorBuilder.newError(env)
                    .message(e.getMessage())
                    .errorType(ErrorType.NOT_FOUND)
                    .build();
            case EmailAlreadyExistException emailAlreadyExistException -> GraphqlErrorBuilder.newError(env)
                    .message(e.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();
            case InvalidTransactionTypeException invalidTransactionTypeException -> GraphqlErrorBuilder.newError(env)
                    .message(e.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();
            case ConstraintViolationException constraintViolationException -> GraphqlErrorBuilder.newError(env)
                    .message(e.getMessage())
                    .errorType(ErrorType.BAD_REQUEST)
                    .build();
            default -> GraphqlErrorBuilder.newError(env)
                    .message("Internal Server Error")
                    .errorType(ErrorType.INTERNAL_ERROR)
                    .build();
        };

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
