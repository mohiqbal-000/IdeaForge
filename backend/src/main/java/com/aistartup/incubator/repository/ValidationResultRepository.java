package com.aistartup.incubator.repository;

import com.aistartup.incubator.model.ValidationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValidationResultRepository extends JpaRepository<ValidationResult, Long> {
    List<ValidationResult> findByStartupId(Long startupId);
}
