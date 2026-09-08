package com.aistartup.incubator.repository;

import com.aistartup.incubator.model.MvpPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MvpPlanRepository extends JpaRepository<MvpPlan, Long> {
    List<MvpPlan> findByStartupId(Long startupId);
}
