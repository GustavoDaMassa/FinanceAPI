package com.gustavohenrique.financeApi.domain.models;

import com.gustavohenrique.financeApi.domain.enums.AggregatorType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "financial_integrations")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialIntegration {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Enumerated(EnumType.STRING)
    private AggregatorType aggregator;

    @Setter private String linkId;
    @Setter private String status;
    @Setter private LocalDateTime createdAt;
    @Setter private LocalDateTime expiresAt;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @OneToMany(mappedBy = "integration")
    private List<Account> accounts;

}
