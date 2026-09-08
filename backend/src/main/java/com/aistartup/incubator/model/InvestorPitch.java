package com.aistartup.incubator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "investor_pitches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorPitch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @Column(columnDefinition = "TEXT") private String elevatorPitch;
    @Column(columnDefinition = "TEXT") private String problemStatement;
    @Column(columnDefinition = "TEXT") private String solution;
    @Column(columnDefinition = "TEXT") private String marketOpportunity;
    @Column(columnDefinition = "TEXT") private String businessModelSummary;
    @Column(columnDefinition = "TEXT") private String askAndUseOfFunds;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
