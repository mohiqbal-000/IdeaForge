package com.aistartup.incubator.repository;

import com.aistartup.incubator.model.InvestorPitch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvestorPitchRepository extends JpaRepository<InvestorPitch, Long> {
    List<InvestorPitch> findByStartupId(Long startupId);
}
