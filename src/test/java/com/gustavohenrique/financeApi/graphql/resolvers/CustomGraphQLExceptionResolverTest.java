package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.exception.EmailAlreadyExistException;
import com.gustavohenrique.financeApi.exception.InvalidTransactionTypeException;
import com.gustavohenrique.financeApi.exception.NotFoundException;
import graphql.GraphQLError;
import graphql.execution.ExecutionStepInfo;
import graphql.language.Field;
import graphql.language.SourceLocation;
import graphql.schema.DataFetchingEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.graphql.execution.ErrorType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomGraphQLExceptionResolverTest {

    private CustomGraphQLExceptionResolver resolver;

    @Mock
    private DataFetchingEnvironment env;
    @Mock
    private Field field;
    @Mock
    private ExecutionStepInfo executionStepInfo;

    @BeforeEach
    void setUp() {
        resolver = new CustomGraphQLExceptionResolver();
        when(env.getField()).thenReturn(field);
        when(field.getSourceLocation()).thenReturn(new SourceLocation(1, 1));
        when(env.getExecutionStepInfo()).thenReturn(executionStepInfo);
        when(executionStepInfo.getPath()).thenReturn(graphql.execution.ResultPath.rootPath());
    }

    @Test
    @DisplayName("Should map NotFoundException to NOT_FOUND")
    void resolveNotFoundException() {
        NotFoundException ex = new NotFoundException("User not found");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertNotNull(error);
        assertEquals("User not found", error.getMessage());
        assertEquals(ErrorType.NOT_FOUND, error.getErrorType());
    }

    @Test
    @DisplayName("Should map EmailAlreadyExistException to BAD_REQUEST")
    void resolveEmailAlreadyExist() {
        EmailAlreadyExistException ex = new EmailAlreadyExistException("test@test.com");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertNotNull(error);
        assertEquals(ErrorType.BAD_REQUEST, error.getErrorType());
    }

    @Test
    @DisplayName("Should map InvalidTransactionTypeException to BAD_REQUEST")
    void resolveInvalidTransactionType() {
        InvalidTransactionTypeException ex = new InvalidTransactionTypeException("INVALID");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertNotNull(error);
        assertEquals(ErrorType.BAD_REQUEST, error.getErrorType());
    }

    @Test
    @DisplayName("Should map unknown exception to INTERNAL_ERROR")
    void resolveUnknownException() {
        RuntimeException ex = new RuntimeException("unexpected");

        GraphQLError error = resolver.resolveToSingleError(ex, env);

        assertNotNull(error);
        assertEquals("Internal Server Error", error.getMessage());
        assertEquals(ErrorType.INTERNAL_ERROR, error.getErrorType());
    }
}
