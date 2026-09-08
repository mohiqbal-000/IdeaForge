package com.aistartup.incubator.controller;

import com.aistartup.incubator.model.InvestorPitch;
import com.aistartup.incubator.service.PitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pitch")
@RequiredArgsConstructor
public class PitchController {

    private final PitchService pitchService;

    @PostMapping("/{startupId}")
    public ResponseEntity<InvestorPitch> generate(@PathVariable Long startupId) {
        return ResponseEntity.ok(pitchService.generatePitch(startupId));
    }

    @GetMapping("/{startupId}")
    public ResponseEntity<InvestorPitch> latest(@PathVariable Long startupId) {
        return ResponseEntity.ok(pitchService.getLatest(startupId));
    }
}
