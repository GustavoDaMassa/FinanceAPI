package com.gustavohenrique.financeApi.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter private String accountName;
    @Setter private String institution;
    @Setter
    @Column(columnDefinition = "TEXT")
    private String description;
    @Setter @Builder.Default private BigDecimal balance = BigDecimal.ZERO;

    @Column(unique = true)
    private String pluggyAccountId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @ManyToOne(optional = true)
    @JoinColumn(name = "integration_id")
    private FinancialIntegration integration;

    @Setter
    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;
}
