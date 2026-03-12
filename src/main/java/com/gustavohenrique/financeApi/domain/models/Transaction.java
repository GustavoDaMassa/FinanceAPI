package com.gustavohenrique.financeApi.domain.models;

import com.gustavohenrique.financeApi.domain.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private BigDecimal amount;

    @Setter
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Setter
    private String description;

    @Setter
    private String source;

    @Setter
    private String destination;

    @Setter
    private LocalDate transactionDate;

    @Setter
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "external_id", unique = true)
    private String externalId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
