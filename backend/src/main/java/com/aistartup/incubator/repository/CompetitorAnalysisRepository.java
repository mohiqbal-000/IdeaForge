package com.aistartup.incubator.repository;

import com.aistartup.incubator.model.CompetitorAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompetitorAnalysisRepository extends JpaRepository<CompetitorAnalysis, Long> {
    List<CompetitorAnalysis> findByStartupId(Long startupId);
}
