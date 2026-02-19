package com.gustavohenrique.financeApi.graphql.resolvers;

import com.gustavohenrique.financeApi.application.interfaces.TransactionService;
import com.gustavohenrique.financeApi.application.wrappers.TransactionQueryResult;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.domain.models.User;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionDTO;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionListWithBalanceDTO;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionPageDTO;
import com.gustavohenrique.financeApi.graphql.inputs.DateRangeInput;
import com.gustavohenrique.financeApi.graphql.inputs.PaginationInput;
import com.gustavohenrique.financeApi.graphql.inputs.TransactionFilterInput;
import com.gustavohenrique.financeApi.graphql.inputs.TransactionInput;
import com.gustavohenrique.financeApi.graphql.mappers.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TransactionResolver {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;


    @QueryMapping
    public TransactionDTO findTransactionById(@Argument Long id) {
        Transaction transaction = transactionService.findById(id);
        return transactionMapper.toDto(transaction);
    }

    @QueryMapping
    public TransactionListWithBalanceDTO listUserTransactions(@AuthenticationPrincipal User user) {
        TransactionQueryResult result = transactionService.listByUserId(user.getId());
        return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
    }

    @QueryMapping
    public TransactionListWithBalanceDTO listAccountTransactions(
            @AuthenticationPrincipal User user,
            @Argument Long accountId) {
        if (accountId != null) {
            var result = transactionService.listByAccount(accountId);
            return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
        }
        var result = transactionService.listByUserId(user.getId());
        return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
    }

    @QueryMapping
    public TransactionListWithBalanceDTO listTransactionsByPeriod(
            @AuthenticationPrincipal User user,
            @Argument Long accountId,
            @Argument DateRangeInput range) {
        if (accountId != null) {
            var result = transactionService.listByPeriod(accountId, range.getStartDate(), range.getEndDate());
            return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
        }
        var result = transactionService.listByPeriodForUser(user.getId(), range.getStartDate(), range.getEndDate());
        return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
    }

    @QueryMapping
    public TransactionListWithBalanceDTO listTransactionsByType(
            @AuthenticationPrincipal User user,
            @Argument Long accountId,
            @Argument String type) {
        if (accountId != null) {
            var result = transactionService.listByType(accountId, type);
            return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
        }
        var result = transactionService.listByTypeForUser(user.getId(), type);
        return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
    }

    @QueryMapping
    public TransactionListWithBalanceDTO listTransactionsByFilter(
            @AuthenticationPrincipal User user,
            @Argument Long accountId,
            @Argument TransactionFilterInput filter) {
        if (accountId != null) {
            var result = transactionService.listByFilter(accountId, filter.getCategoryIds());
            return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
        }
        var result = transactionService.listByFilterForUser(user.getId(), filter.getCategoryIds());
        return transactionMapper.toListWithBalanceDTO(result.getTransactions(), result.getBalance());
    }

    @QueryMapping
    public List<TransactionDTO> listUncategorizedTransactions(
            @AuthenticationPrincipal User user,
            @Argument Long accountId) {
        List<Transaction> transactions = accountId != null
                ? transactionService.listUncategorized(accountId)
                : transactionService.listUncategorizedForUser(user.getId());
        return transactions.stream().map(transactionMapper::toDto).toList();
    }

    @QueryMapping
    public TransactionPageDTO listAccountTransactionsPaginated(
            @AuthenticationPrincipal User user,
            @Argument Long accountId,
            @Argument PaginationInput pagination) {
        PaginationInput p = pagination != null ? pagination : new PaginationInput();
        if (accountId != null) {
            var result = transactionService.listByAccountPaginated(accountId, p.getPage(), p.getSize());
            return transactionMapper.toPageDTO(result);
        }
        var result = transactionService.listByUserPaginated(user.getId(), p.getPage(), p.getSize());
        return transactionMapper.toPageDTO(result);
    }

    @QueryMapping
    public TransactionPageDTO listTransactionsByPeriodPaginated(
            @AuthenticationPrincipal User user,
            @Argument Long accountId,
            @Argument DateRangeInput range,
            @Argument PaginationInput pagination) {
        PaginationInput p = pagination != null ? pagination : new PaginationInput();
        if (accountId != null) {
            var result = transactionService.listByPeriodPaginated(accountId, range.getStartDate(), range.getEndDate(), p.getPage(), p.getSize());
            return transactionMapper.toPageDTO(result);
        }
        var result = transactionService.listByPeriodPaginatedForUser(user.getId(), range.getStartDate(), range.getEndDate(), p.getPage(), p.getSize());
        return transactionMapper.toPageDTO(result);
    }

    @QueryMapping
    public TransactionPageDTO listTransactionsByTypePaginated(
            @AuthenticationPrincipal User user,
            @Argument Long accountId,
            @Argument String type,
            @Argument PaginationInput pagination) {
        PaginationInput p = pagination != null ? pagination : new PaginationInput();
        if (accountId != null) {
            var result = transactionService.listByTypePaginated(accountId, type, p.getPage(), p.getSize());
            return transactionMapper.toPageDTO(result);
        }
        var result = transactionService.listByTypePaginatedForUser(user.getId(), type, p.getPage(), p.getSize());
        return transactionMapper.toPageDTO(result);
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
    public TransactionDTO categorizeTransaction(@Argument Long id, @Argument Long categoryId) {
        Transaction categorized = transactionService.categorize(id, categoryId);
        return transactionMapper.toDto(categorized);
    }

    @MutationMapping
    public TransactionDTO deleteTransaction(@Argument Long id) {
        Transaction deleted = transactionService.delete(id);
        return transactionMapper.toDto(deleted);
    }
}
