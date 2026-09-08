package com.aistartup.incubator.repository;

import com.aistartup.incubator.model.FinancialPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialPlanRepository extends JpaRepository<FinancialPlan, Long> {
    List<FinancialPlan> findByStartupId(Long startupId);
}
