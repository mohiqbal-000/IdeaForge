package com.aistartup.incubator.service;

import com.aistartup.incubator.exception.ApiException;
import com.aistartup.incubator.model.InvestorPitch;
import com.aistartup.incubator.model.Startup;
import com.aistartup.incubator.repository.InvestorPitchRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PitchService {

    private final GeminiService geminiService;
    private final InvestorPitchRepository investorPitchRepository;
    private final StartupService startupService;

    public InvestorPitch generatePitch(Long startupId) {
        Startup startup = startupService.getById(startupId);

        String prompt = """
            You are a pitch coach helping a founder prepare for investor meetings.
            Draft an investor pitch narrative for the startup below.

            Name: %s
            Industry: %s
            Description: %s

            Respond with STRICT JSON only, no markdown, in this exact shape:
            {
              "elevatorPitch": "1-2 sentence hook",
              "problemStatement": "2-3 sentences",
              "solution": "2-3 sentences",
              "marketOpportunity": "2-3 sentences with a rough market size framing",
              "businessModelSummary": "2-3 sentences on how it makes money",
              "askAndUseOfFunds": "2-3 sentences on funding ask and use of funds"
            }
            """.formatted(startup.getName(), startup.getIndustry(), startup.getDescription());

        JsonNode r = geminiService.generateJson(prompt);

        InvestorPitch pitch = InvestorPitch.builder()
                .startup(startup)
                .elevatorPitch(r.path("elevatorPitch").asText(""))
                .problemStatement(r.path("problemStatement").asText(""))
                .solution(r.path("solution").asText(""))
                .marketOpportunity(r.path("marketOpportunity").asText(""))
                .businessModelSummary(r.path("businessModelSummary").asText(""))
                .askAndUseOfFunds(r.path("askAndUseOfFunds").asText(""))
                .build();

        return investorPitchRepository.save(pitch);
    }

    public InvestorPitch getLatest(Long startupId) {
        List<InvestorPitch> results = investorPitchRepository.findByStartupId(startupId);
        if (results.isEmpty()) {
            throw new ApiException("No investor pitch yet for this startup", HttpStatus.NOT_FOUND);
        }
        return results.get(results.size() - 1);
    }
}
