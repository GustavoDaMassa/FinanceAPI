package com.gustavohenrique.financeApi.domain.models;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String description;
    private String source;
    private String destination;

    private LocalDate transactionDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private Category subcategory;


    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
