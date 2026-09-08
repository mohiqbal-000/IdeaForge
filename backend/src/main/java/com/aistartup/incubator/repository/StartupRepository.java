package com.aistartup.incubator.repository;

import com.aistartup.incubator.model.Startup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StartupRepository extends JpaRepository<Startup, Long> {
    List<Startup> findByUserId(Long userId);
}
