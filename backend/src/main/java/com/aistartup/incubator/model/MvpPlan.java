package com.aistartup.incubator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "mvp_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MvpPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @Column(columnDefinition = "TEXT")
    private String coreFeatures; // newline separated

    @Column(columnDefinition = "TEXT")
    private String recommendedTechStack;

    @Column(columnDefinition = "TEXT")
    private String milestones; // JSON string: [{phase, duration, deliverable}, ...]

    private String estimatedTimeline; // e.g. "8-10 weeks"

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
