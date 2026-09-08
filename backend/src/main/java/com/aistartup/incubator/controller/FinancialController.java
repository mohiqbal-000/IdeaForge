package com.aistartup.incubator.controller;

import com.aistartup.incubator.model.FinancialPlan;
import com.aistartup.incubator.service.FinancialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/financial-plan")
@RequiredArgsConstructor
public class FinancialController {

    private final FinancialService financialService;

    @PostMapping("/{startupId}")
    public ResponseEntity<FinancialPlan> generate(@PathVariable Long startupId) {
        return ResponseEntity.ok(financialService.generatePlan(startupId));
    }

    @GetMapping("/{startupId}")
    public ResponseEntity<FinancialPlan> latest(@PathVariable Long startupId) {
        return ResponseEntity.ok(financialService.getLatest(startupId));
    }
}
