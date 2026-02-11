package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.application.wrappers.TransactionPageResult;
import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.graphql.dtos.PageInfo;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionDTO;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionListWithBalanceDTO;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionPageDTO;
import com.gustavohenrique.financeApi.graphql.inputs.TransactionInput;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionMapper {

    private final ModelMapper modelMapper;

    public TransactionDTO toDto(Transaction transaction) {
        TransactionDTO dto = modelMapper.map(transaction, TransactionDTO.class);
        dto.setAmount(transaction.getAmount().toPlainString());
        dto.setTransactionDate(transaction.getTransactionDate().toString());
        dto.setAccountId(transaction.getAccount().getId());

        if (transaction.getCategory() != null)
            dto.setCategoryId(transaction.getCategory().getId());

        return dto;
    }

    public Transaction fromInput(TransactionInput input) {
        Transaction transaction = new Transaction();

        transaction.setAmount(new BigDecimal(input.getAmount()));
        transaction.setType(input.getType());
        transaction.setDescription(input.getDescription());
        transaction.setSource(input.getSource());
        transaction.setDestination(input.getDestination());
        transaction.setTransactionDate(LocalDate.parse(input.getTransactionDate()));

        Account account = new Account();
        account.setId(input.getAccountId());
        transaction.setAccount(account);

        if (input.getCategoryId() != null) {
            Category category = new Category();
            category.setId(input.getCategoryId());
            transaction.setCategory(category);
        }

        return transaction;
    }

    public TransactionListWithBalanceDTO toListWithBalanceDTO(List<Transaction> transactions, BigDecimal balance) {
        TransactionListWithBalanceDTO dto = new TransactionListWithBalanceDTO();
        dto.setTransactions(transactions.stream().map(this::toDto).toList());
        dto.setBalance(balance.toPlainString());
        return dto;
    }

    public TransactionPageDTO toPageDTO(TransactionPageResult result) {
        Page<Transaction> page = result.getPage();

        PageInfo pageInfo = PageInfo.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();

        return TransactionPageDTO.builder()
                .transactions(page.getContent().stream().map(this::toDto).toList())
                .pageInfo(pageInfo)
                .balance(result.getBalance())
                .build();
    }
}
