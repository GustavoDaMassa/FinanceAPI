package com.gustavohenrique.financeApi.graphql.mappers;

import com.gustavohenrique.financeApi.domain.models.Account;
import com.gustavohenrique.financeApi.domain.models.Category;
import com.gustavohenrique.financeApi.domain.models.Transaction;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionDTO;
import com.gustavohenrique.financeApi.graphql.dtos.TransactionListWithBalanceDTO;
import com.gustavohenrique.financeApi.graphql.inputs.TransactionInput;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

        if (transaction.getSubcategory() != null)
            dto.setSubcategoryId(transaction.getSubcategory().getId());

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

        if (input.getSubcategoryId() != null) {
            Category subcategory = new Category();
            subcategory.setId(input.getSubcategoryId());
            transaction.setSubcategory(subcategory);
        }

        return transaction;
    }

    public TransactionListWithBalanceDTO toListWithBalanceDTO(List<Transaction> transactions, BigDecimal balance) {
        TransactionListWithBalanceDTO dto = new TransactionListWithBalanceDTO();
        dto.setTransactions(transactions.stream().map(this::toDto).toList());
        dto.setBalance(balance.toPlainString());
        return dto;
    }
}
