package com.aistartup.incubator.service;

import com.aistartup.incubator.dto.IdeaGenerationRequest;
import com.aistartup.incubator.model.StartupIdea;
import com.aistartup.incubator.model.User;
import com.aistartup.incubator.repository.StartupIdeaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final GeminiService geminiService;
    private final StartupIdeaRepository ideaRepository;

    public List<StartupIdea> generateIdeas(User user, IdeaGenerationRequest request) {
        String prompt = """
            You are a startup ideation expert. Based on the entrepreneur's interests and
            constraints below, propose exactly 3 distinct, viable startup ideas.

            Interests: %s
            Problem area (optional): %s
            Target market (optional): %s

            Respond with STRICT JSON only, no markdown, in this exact shape:
            {
              "ideas": [
                {
                  "title": "string",
                  "description": "2-3 sentence description",
                  "targetAudience": "who this is for",
                  "uniqueValueProposition": "why this idea wins"
                }
              ]
            }
            """.formatted(
                request.getInterests(),
                request.getProblemArea() == null ? "not specified" : request.getProblemArea(),
                request.getTargetMarket() == null ? "not specified" : request.getTargetMarket()
        );

        JsonNode result = geminiService.generateJson(prompt);
        List<StartupIdea> savedIdeas = new ArrayList<>();

        if (result.has("ideas")) {
            for (JsonNode ideaNode : result.get("ideas")) {
                StartupIdea idea = StartupIdea.builder()
                        .user(user)
                        .title(ideaNode.path("title").asText("Untitled idea"))
                        .description(ideaNode.path("description").asText(""))
                        .targetAudience(ideaNode.path("targetAudience").asText(""))
                        .uniqueValueProposition(ideaNode.path("uniqueValueProposition").asText(""))
                        .build();
                savedIdeas.add(ideaRepository.save(idea));
            }
        }
        return savedIdeas;
    }

    public List<StartupIdea> listForUser(User user) {
        return ideaRepository.findByUserId(user.getId());
    }
}
