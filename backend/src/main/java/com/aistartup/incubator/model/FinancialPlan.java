package com.aistartup.incubator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "financial_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    private Double estimatedStartupCost;
    private Double fundingNeeded;
    private Integer breakEvenMonth;

    @Column(columnDefinition = "TEXT")
    private String revenueProjection; // JSON string: [{month, revenue, expenses}, ...]

    @Column(columnDefinition = "TEXT")
    private String assumptions;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
