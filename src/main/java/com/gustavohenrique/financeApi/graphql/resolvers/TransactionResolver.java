package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.application.results.TransactionQueryResult;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionDTO;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionListWithBalanceDTO;
import com.gustavohenrique.financeApi.graphql.inputs.DateRangeInput;
import com.gustavohenrique.financeApi.graphql.inputs.TransactionFilterInput;
import com.gustavohenrique.financeApi.graphql.inputs.TransactionInput;
import com.gustavohenrique.financeApi.graphql.mappers.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TransactionResolver {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;


    @QueryMapping
    public TransactionListWithBalanceDTO listUserTransactions(@Argument Long userId) {
        TransactionQueryResult result = transactionService.listByUserId(userId);
        return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
    }

    @QueryMapping
    public TransactionListWithBalanceDTO listAccountTransactions(@Argument Long accountId) {
        var result = transactionService.listByAccount(accountId);
        return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
    }

    @QueryMapping
    public TransactionListWithBalanceDTO listTransactionsByPeriod(@Argument Long accountId, @Argument DateRangeInput range) {
        var result = transactionService.listByPeriod(accountId, range.getStartDate(), range.getEndDate());
        return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
    }

    @QueryMapping
    public TransactionListWithBalanceDTO listTransactionsByType(@Argument Long accountId, @Argument String type) {
        var result = transactionService.listByType(accountId, type);
        return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
    }

    @QueryMapping
    public TransactionListWithBalanceDTO listTransactionsByFilter(@Argument Long accountId, @Argument TransactionFilterInput filter) {
        var result = transactionService.listByFilter(accountId, filter.getCategoryIds(), filter.getSubcategoryIds());
        return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
    }

    @QueryMapping
    public List<TransactionDTO> listUncategorizedTransactions(@Argument Long accountId) {
        return transactionService.listUncategorized(accountId)
                .stream()
                .map(transactionMapper::toDto)
                .toList();
    }

//----------------------------------------------------------------------------------------

    @MutationMapping
    public TransactionDTO createTransaction(@Argument TransactionInput input) {
        Transaction transaction = transactionMapper.fromInput(input);
        Transaction created = transactionService.create(transaction);
        return transactionMapper.toDto(created);
    }

    @MutationMapping
    public TransactionDTO updateTransaction(@Argument Long id, @Argument TransactionInput input) {
        Transaction transaction = transactionMapper.fromInput(input);
        Transaction updated = transactionService.update(id, transaction);
        return transactionMapper.toDto(updated);
    }

    @MutationMapping
    public TransactionDTO categorizeTransaction(@Argument Long id, @Argument Long categoryId, @Argument Long subcategoryId) {
        Transaction categorized = transactionService.categorize(id, categoryId, subcategoryId);
        return transactionMapper.toDto(categorized);
    }

    @MutationMapping
    public TransactionDTO deleteTransaction(@Argument Long id) {
        Transaction deleted = transactionService.delete(id);
        return transactionMapper.toDto(deleted);
    }
}
