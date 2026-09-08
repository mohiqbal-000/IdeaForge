package com.aistartup.incubator.repository;

import com.aistartup.incubator.model.BusinessModelCanvas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessModelCanvasRepository extends JpaRepository<BusinessModelCanvas, Long> {
    List<BusinessModelCanvas> findByStartupId(Long startupId);
}
