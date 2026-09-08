package com.aistartup.incubator.controller;

import com.aistartup.incubator.dto.IdeaGenerationRequest;
import com.aistartup.incubator.model.StartupIdea;
import com.aistartup.incubator.model.User;
import com.aistartup.incubator.service.IdeaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ideas")
@RequiredArgsConstructor
public class IdeaController {

    private final IdeaService ideaService;

    @PostMapping("/generate")
    public ResponseEntity<List<StartupIdea>> generate(@AuthenticationPrincipal User user,
                                                        @Valid @RequestBody IdeaGenerationRequest request) {
        return ResponseEntity.ok(ideaService.generateIdeas(user, request));
    }

    @GetMapping
    public ResponseEntity<List<StartupIdea>> myIdeas(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ideaService.listForUser(user));
    }
}
