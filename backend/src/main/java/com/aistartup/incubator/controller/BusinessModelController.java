package com.aistartup.incubator.controller;

import com.aistartup.incubator.model.BusinessModelCanvas;
import com.aistartup.incubator.service.BusinessModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business-model")
@RequiredArgsConstructor
public class BusinessModelController {

    private final BusinessModelService businessModelService;

    @PostMapping("/{startupId}")
    public ResponseEntity<BusinessModelCanvas> generate(@PathVariable Long startupId) {
        return ResponseEntity.ok(businessModelService.generateCanvas(startupId));
    }

    @GetMapping("/{startupId}")
    public ResponseEntity<BusinessModelCanvas> latest(@PathVariable Long startupId) {
        return ResponseEntity.ok(businessModelService.getLatest(startupId));
    }
}
