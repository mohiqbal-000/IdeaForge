package com.aistartup.incubator.repository;

import com.aistartup.incubator.model.StartupIdea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StartupIdeaRepository extends JpaRepository<StartupIdea, Long> {
    List<StartupIdea> findByUserId(Long userId);
}
