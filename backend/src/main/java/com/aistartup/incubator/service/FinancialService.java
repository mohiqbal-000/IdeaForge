package com.aistartup.incubator.service;

import com.aistartup.incubator.exception.ApiException;
import com.aistartup.incubator.model.FinancialPlan;
import com.aistartup.incubator.model.Startup;
import com.aistartup.incubator.repository.FinancialPlanRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialService {

    private final GeminiService geminiService;
    private final FinancialPlanRepository financialPlanRepository;
    private final StartupService startupService;

    public FinancialPlan generatePlan(Long startupId) {
        Startup startup = startupService.getById(startupId);

        String prompt = """
            You are a startup financial planner. Produce a realistic early-stage
            financial estimate for the startup below, covering the first 12 months.

            Name: %s
            Industry: %s
            Description: %s

            Respond with STRICT JSON only, no markdown, in this exact shape:
            {
              "estimatedStartupCost": number (USD),
              "fundingNeeded": number (USD),
              "breakEvenMonth": integer (1-24),
              "revenueProjection": [
                {"month": 1, "revenue": number, "expenses": number}
              ],
              "assumptions": "2-4 sentences describing key assumptions"
            }
            Provide 12 entries in revenueProjection, one per month.
            """.formatted(startup.getName(), startup.getIndustry(), startup.getDescription());

        JsonNode r = geminiService.generateJson(prompt);

        FinancialPlan plan = FinancialPlan.builder()
                .startup(startup)
                .estimatedStartupCost(r.path("estimatedStartupCost").asDouble(0))
                .fundingNeeded(r.path("fundingNeeded").asDouble(0))
                .breakEvenMonth(r.path("breakEvenMonth").asInt(0))
                .revenueProjection(r.has("revenueProjection") ? r.get("revenueProjection").toString() : "[]")
                .assumptions(r.path("assumptions").asText(""))
                .build();

        return financialPlanRepository.save(plan);
    }

    public FinancialPlan getLatest(Long startupId) {
        List<FinancialPlan> results = financialPlanRepository.findByStartupId(startupId);
        if (results.isEmpty()) {
            throw new ApiException("No financial plan yet for this startup", HttpStatus.NOT_FOUND);
        }
        return results.get(results.size() - 1);
    }
}
