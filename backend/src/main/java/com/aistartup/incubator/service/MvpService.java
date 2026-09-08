package com.aistartup.incubator.service;

import com.aistartup.incubator.exception.ApiException;
import com.aistartup.incubator.model.MvpPlan;
import com.aistartup.incubator.model.Startup;
import com.aistartup.incubator.repository.MvpPlanRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MvpService {

    private final GeminiService geminiService;
    private final MvpPlanRepository mvpPlanRepository;
    private final StartupService startupService;

    public MvpPlan generatePlan(Long startupId) {
        Startup startup = startupService.getById(startupId);

        String prompt = """
            You are a product/technology advisor. Define an MVP roadmap for the
            startup below, appropriate for a small team.

            Name: %s
            Industry: %s
            Description: %s

            Respond with STRICT JSON only, no markdown, in this exact shape:
            {
              "coreFeatures": "newline-separated list of 4-6 MVP features",
              "recommendedTechStack": "newline-separated list of recommended technologies with rationale",
              "estimatedTimeline": "e.g. '8-10 weeks'",
              "milestones": [
                {"phase": "string", "duration": "string", "deliverable": "string"}
              ]
            }
            Provide 4-5 milestones.
            """.formatted(startup.getName(), startup.getIndustry(), startup.getDescription());

        JsonNode r = geminiService.generateJson(prompt);

        MvpPlan plan = MvpPlan.builder()
                .startup(startup)
                .coreFeatures(r.path("coreFeatures").asText(""))
                .recommendedTechStack(r.path("recommendedTechStack").asText(""))
                .estimatedTimeline(r.path("estimatedTimeline").asText(""))
                .milestones(r.has("milestones") ? r.get("milestones").toString() : "[]")
                .build();

        return mvpPlanRepository.save(plan);
    }

    public MvpPlan getLatest(Long startupId) {
        List<MvpPlan> results = mvpPlanRepository.findByStartupId(startupId);
        if (results.isEmpty()) {
            throw new ApiException("No MVP plan yet for this startup", HttpStatus.NOT_FOUND);
        }
        return results.get(results.size() - 1);
    }
}
