package com.aistartup.incubator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Thin wrapper around Google's Gemini generateContent API.
 * Every AI-powered module (idea generation, validation, business model
 * canvas, competitor analysis, financial planning, MVP roadmap, investor
 * pitch, report copy) funnels its prompt through this single service so
 * the model, retry policy, and response parsing only live in one place.
 */
@Slf4j
@Service
public class GeminiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.gemini.api-key}")
    private String apiKey;

    @Value("${app.gemini.base-url}")
    private String baseUrl;

    /**
     * Sends a prompt to Gemini and returns the raw text response.
     * Callers that need structured data should instruct the prompt to
     * respond in strict JSON and parse the result themselves (see
     * generateJson below).
     */
    public String generateText(String prompt) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("GEMINI_API_KEY not configured — returning placeholder content.");
            return "[AI content unavailable: set GEMINI_API_KEY to enable live generation]";
        }

        String url = baseUrl + "?key=" + apiKey;

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText();
        } catch (Exception e) {
            log.error("Gemini API call failed", e);
            return "[AI generation failed: " + e.getMessage() + "]";
        }
    }

    /**
     * Convenience wrapper for prompts that ask Gemini to respond with
     * strict JSON. Strips markdown code fences if the model wraps its
     * answer in ```json ... ``` despite instructions.
     */
    public JsonNode generateJson(String prompt) {
        String raw = generateText(prompt);
        String cleaned = raw.replaceAll("(?s)```json", "").replaceAll("(?s)```", "").trim();
        try {
            return objectMapper.readTree(cleaned);
        } catch (Exception e) {
            log.error("Failed to parse Gemini JSON response: {}", cleaned, e);
            return objectMapper.createObjectNode().put("error", "Failed to parse AI response");
        }
    }
}
