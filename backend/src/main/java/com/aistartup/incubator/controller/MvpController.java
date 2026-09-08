package com.aistartup.incubator.controller;

import com.aistartup.incubator.model.MvpPlan;
import com.aistartup.incubator.service.MvpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mvp-plan")
@RequiredArgsConstructor
public class MvpController {

    private final MvpService mvpService;

    @PostMapping("/{startupId}")
    public ResponseEntity<MvpPlan> generate(@PathVariable Long startupId) {
        return ResponseEntity.ok(mvpService.generatePlan(startupId));
    }

    @GetMapping("/{startupId}")
    public ResponseEntity<MvpPlan> latest(@PathVariable Long startupId) {
        return ResponseEntity.ok(mvpService.getLatest(startupId));
    }
}
