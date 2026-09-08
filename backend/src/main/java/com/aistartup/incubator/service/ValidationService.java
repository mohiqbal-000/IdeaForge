package com.aistartup.incubator.service;

import com.aistartup.incubator.exception.ApiException;
import com.aistartup.incubator.model.Startup;
import com.aistartup.incubator.model.ValidationResult;
import com.aistartup.incubator.repository.ValidationResultRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidationService {

    private final GeminiService geminiService;
    private final ValidationResultRepository validationResultRepository;
    private final StartupService startupService;

    public ValidationResult validateStartup(Long startupId) {
        Startup startup = startupService.getById(startupId);

        String prompt = """
            You are a startup validation analyst. Assess the viability of the startup
            described below.

            Name: %s
            Industry: %s
            Description: %s

            Respond with STRICT JSON only, no markdown, in this exact shape:
            {
              "feasibilityScore": 0-100 integer,
              "marketSizeAnalysis": "2-4 sentences estimating TAM/SAM/SOM qualitatively",
              "risks": "bullet-style list of key risks as a single string separated by newlines",
              "recommendations": "bullet-style list of next steps as a single string separated by newlines"
            }
            """.formatted(startup.getName(), startup.getIndustry(), startup.getDescription());

        JsonNode result = geminiService.generateJson(prompt);

        ValidationResult validationResult = ValidationResult.builder()
                .startup(startup)
                .feasibilityScore(result.path("feasibilityScore").asInt(0))
                .marketSizeAnalysis(result.path("marketSizeAnalysis").asText(""))
                .risks(result.path("risks").asText(""))
                .recommendations(result.path("recommendations").asText(""))
                .build();

        return validationResultRepository.save(validationResult);
    }

    public ValidationResult getLatest(Long startupId) {
        List<ValidationResult> results = validationResultRepository.findByStartupId(startupId);
        if (results.isEmpty()) {
            throw new ApiException("No validation results yet for this startup", HttpStatus.NOT_FOUND);
        }
        return results.get(results.size() - 1);
    }
}
