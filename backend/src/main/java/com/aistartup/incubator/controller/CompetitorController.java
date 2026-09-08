package com.aistartup.incubator.controller;

import com.aistartup.incubator.model.CompetitorAnalysis;
import com.aistartup.incubator.service.CompetitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/competitors")
@RequiredArgsConstructor
public class CompetitorController {

    private final CompetitorService competitorService;

    @PostMapping("/{startupId}")
    public ResponseEntity<CompetitorAnalysis> analyze(@PathVariable Long startupId) {
        return ResponseEntity.ok(competitorService.analyze(startupId));
    }

    @GetMapping("/{startupId}")
    public ResponseEntity<CompetitorAnalysis> latest(@PathVariable Long startupId) {
        return ResponseEntity.ok(competitorService.getLatest(startupId));
    }
}
