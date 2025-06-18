package com.gustavohenrique.financeApi.domain.models;

import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "financial_integrations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancialIntegration {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AggregatorType aggregator;

    private String linkId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "integration")
    private List<Account> accounts;

}
