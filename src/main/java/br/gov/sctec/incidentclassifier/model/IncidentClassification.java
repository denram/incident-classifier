package br.gov.sctec.incidentclassifier.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IncidentClassification {

    @NotBlank
    private String formalizedIncidentText;

    @NotNull
    private IncidentCategory category;

    @NotNull
    private IncidentSeverity severity;

    @NotNull
    private LocalDateTime registeredAt;
}
