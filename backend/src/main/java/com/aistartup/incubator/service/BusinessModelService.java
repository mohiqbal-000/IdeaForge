package com.aistartup.incubator.service;

import com.aistartup.incubator.exception.ApiException;
import com.aistartup.incubator.model.BusinessModelCanvas;
import com.aistartup.incubator.model.Startup;
import com.aistartup.incubator.repository.BusinessModelCanvasRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusinessModelService {

    private final GeminiService geminiService;
    private final BusinessModelCanvasRepository canvasRepository;
    private final StartupService startupService;

    public BusinessModelCanvas generateCanvas(Long startupId) {
        Startup startup = startupService.getById(startupId);

        String prompt = """
            You are a business strategy consultant. Build a complete Business Model
            Canvas for the startup below, following Osterwalder's 9-block framework.

            Name: %s
            Industry: %s
            Description: %s

            Respond with STRICT JSON only, no markdown, in this exact shape:
            {
              "keyPartners": "string",
              "keyActivities": "string",
              "keyResources": "string",
              "valuePropositions": "string",
              "customerRelationships": "string",
              "channels": "string",
              "customerSegments": "string",
              "costStructure": "string",
              "revenueStreams": "string"
            }
            Each value should be 2-4 concise bullet points joined by newlines.
            """.formatted(startup.getName(), startup.getIndustry(), startup.getDescription());

        JsonNode r = geminiService.generateJson(prompt);

        BusinessModelCanvas canvas = BusinessModelCanvas.builder()
                .startup(startup)
                .keyPartners(r.path("keyPartners").asText(""))
                .keyActivities(r.path("keyActivities").asText(""))
                .keyResources(r.path("keyResources").asText(""))
                .valuePropositions(r.path("valuePropositions").asText(""))
                .customerRelationships(r.path("customerRelationships").asText(""))
                .channels(r.path("channels").asText(""))
                .customerSegments(r.path("customerSegments").asText(""))
                .costStructure(r.path("costStructure").asText(""))
                .revenueStreams(r.path("revenueStreams").asText(""))
                .build();

        return canvasRepository.save(canvas);
    }

    public BusinessModelCanvas getLatest(Long startupId) {
        List<BusinessModelCanvas> results = canvasRepository.findByStartupId(startupId);
        if (results.isEmpty()) {
            throw new ApiException("No business model canvas yet for this startup", HttpStatus.NOT_FOUND);
        }
        return results.get(results.size() - 1);
    }
}
