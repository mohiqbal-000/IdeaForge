package com.aistartup.incubator.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "business_model_canvases")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessModelCanvas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;

    @Column(columnDefinition = "TEXT") private String keyPartners;
    @Column(columnDefinition = "TEXT") private String keyActivities;
    @Column(columnDefinition = "TEXT") private String keyResources;
    @Column(columnDefinition = "TEXT") private String valuePropositions;
    @Column(columnDefinition = "TEXT") private String customerRelationships;
    @Column(columnDefinition = "TEXT") private String channels;
    @Column(columnDefinition = "TEXT") private String customerSegments;
    @Column(columnDefinition = "TEXT") private String costStructure;
    @Column(columnDefinition = "TEXT") private String revenueStreams;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
