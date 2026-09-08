package com.aistartup.incubator.controller;

import com.aistartup.incubator.dto.StartupRequest;
import com.aistartup.incubator.model.Startup;
import com.aistartup.incubator.model.User;
import com.aistartup.incubator.service.StartupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/startups")
@RequiredArgsConstructor
public class StartupController {

    private final StartupService startupService;

    @PostMapping
    public ResponseEntity<Startup> create(@AuthenticationPrincipal User user, @Valid @RequestBody StartupRequest request) {
        return ResponseEntity.ok(startupService.create(user, request));
    }

    @GetMapping
    public ResponseEntity<List<Startup>> listMine(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(startupService.listForUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Startup> get(@PathVariable Long id) {
        return ResponseEntity.ok(startupService.getById(id));
    }
}
