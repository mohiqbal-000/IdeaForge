package com.aistartup.incubator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StartupRequest {
    @NotBlank
    private String name;
    private String description;
    private String industry;
}
