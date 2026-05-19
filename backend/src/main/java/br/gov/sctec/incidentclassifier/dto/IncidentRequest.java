package br.gov.sctec.incidentclassifier.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidentRequest {

    @NotBlank(message = "Incident description must not be blank")
    private String incident;
}
