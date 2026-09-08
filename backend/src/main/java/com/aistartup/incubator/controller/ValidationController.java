package com.aistartup.incubator.controller;

import com.aistartup.incubator.model.ValidationResult;
import com.aistartup.incubator.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/validation")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;

    @PostMapping("/{startupId}")
    public ResponseEntity<ValidationResult> validate(@PathVariable Long startupId) {
        return ResponseEntity.ok(validationService.validateStartup(startupId));
    }

    @GetMapping("/{startupId}")
    public ResponseEntity<ValidationResult> latest(@PathVariable Long startupId) {
        return ResponseEntity.ok(validationService.getLatest(startupId));
    }
}
