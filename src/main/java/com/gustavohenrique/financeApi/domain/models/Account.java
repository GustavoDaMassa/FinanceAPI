package com.gustavohenrique.financeApi.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountName;
    private String institution;
    private String description;
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(unique = true)
    private String pluggyAccountId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = true)
    @JoinColumn(name = "integration_id")
    private FinancialIntegration integration;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;
}
