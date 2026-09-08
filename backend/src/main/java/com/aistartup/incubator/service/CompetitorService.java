package com.aistartup.incubator.service;

import com.aistartup.incubator.exception.ApiException;
import com.aistartup.incubator.model.CompetitorAnalysis;
import com.aistartup.incubator.model.Startup;
import com.aistartup.incubator.repository.CompetitorAnalysisRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompetitorService {

    private final GeminiService geminiService;
    private final CompetitorAnalysisRepository competitorAnalysisRepository;
    private final StartupService startupService;

    public CompetitorAnalysis analyze(Long startupId) {
        Startup startup = startupService.getById(startupId);

        String prompt = """
            You are a competitive intelligence analyst. Identify 3 plausible competitors
            for the startup below (real or representative of the space) and assess
            market positioning.

            Name: %s
            Industry: %s
            Description: %s

            Respond with STRICT JSON only, no markdown, in this exact shape:
            {
              "competitors": [
                {"name": "string", "strengths": "string", "weaknesses": "string"}
              ],
              "marketPosition": "2-3 sentences on where this startup sits in the landscape",
              "differentiationStrategy": "2-3 sentences on how to differentiate"
            }
            """.formatted(startup.getName(), startup.getIndustry(), startup.getDescription());

        JsonNode r = geminiService.generateJson(prompt);

        CompetitorAnalysis analysis = CompetitorAnalysis.builder()
                .startup(startup)
                .competitors(r.has("competitors") ? r.get("competitors").toString() : "[]")
                .marketPosition(r.path("marketPosition").asText(""))
                .differentiationStrategy(r.path("differentiationStrategy").asText(""))
                .build();

        return competitorAnalysisRepository.save(analysis);
    }

    public CompetitorAnalysis getLatest(Long startupId) {
        List<CompetitorAnalysis> results = competitorAnalysisRepository.findByStartupId(startupId);
        if (results.isEmpty()) {
            throw new ApiException("No competitor analysis yet for this startup", HttpStatus.NOT_FOUND);
        }
        return results.get(results.size() - 1);
    }
}
