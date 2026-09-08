package com.aistartup.incubator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "competitor_analyses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitorAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @Column(columnDefinition = "TEXT")
    private String competitors; // JSON string: [{name, strengths, weaknesses}, ...]

    @Column(columnDefinition = "TEXT")
    private String marketPosition;

    @Column(columnDefinition = "TEXT")
    private String differentiationStrategy;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
