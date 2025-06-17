package com.gustavohenrique.financeApi.models;

import com.gustavohenrique.financeApi.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String classification;
    private String specification;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
