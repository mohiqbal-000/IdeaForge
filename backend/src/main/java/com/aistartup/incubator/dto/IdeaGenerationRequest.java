package com.aistartup.incubator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class IdeaGenerationRequest {
    @NotBlank
    private String interests;      // e.g. "fintech, sustainability"
    private String problemArea;    // optional problem the user wants to solve
    private String targetMarket;   // optional
}
