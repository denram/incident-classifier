package br.gov.sctec.incidentclassifier.service;

import br.gov.sctec.incidentclassifier.exception.IncidentClassificationException;
import br.gov.sctec.incidentclassifier.model.IncidentClassification;
import br.gov.sctec.incidentclassifier.model.IncidentRecord;
import br.gov.sctec.incidentclassifier.provider.ApiProvider;
import br.gov.sctec.incidentclassifier.repository.IncidentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class IncidentClassifierService {

    private final ApiProvider apiProvider;
    private final String systemPrompt;
    private final ObjectMapper objectMapper;
    private final IncidentRepository incidentRepository;

    public IncidentClassifierService(
            ApiProvider apiProvider,
            @Qualifier("incidentClassifierSystemPrompt") String systemPrompt,
            ObjectMapper objectMapper,
            IncidentRepository incidentRepository) {
        this.apiProvider = apiProvider;
        this.systemPrompt = systemPrompt;
        this.objectMapper = objectMapper;
        this.incidentRepository = incidentRepository;
    }

    public IncidentClassification incidentClassifier(String userPrompt) {
        log.info("Starting incident classification");

        String aiResponse = apiProvider.getResponse(systemPrompt, userPrompt, null);
        String cleanedResponse = cleanJsonResponse(aiResponse);

        try {
            IncidentClassification classification = objectMapper.readValue(cleanedResponse, IncidentClassification.class);
            log.info("Incident classified: category={}, severity={}", classification.getCategory(), classification.getSeverity());

            incidentRepository.save(IncidentRecord.builder()
                    .originalText(userPrompt)
                    .formalizedIncidentText(classification.getFormalizedIncidentText())
                    .category(classification.getCategory())
                    .severity(classification.getSeverity())
                    .registeredAt(classification.getRegisteredAt())
                    .suggestedAction(classification.getSuggestedAction())
                    .build());

            return classification;
        } catch (JsonProcessingException e) {
            log.error("Failed to parse AI response into IncidentClassification. Response: {}", cleanedResponse, e);
            throw new IncidentClassificationException(
                    "Failed to parse AI response into IncidentClassification: " + e.getMessage(), e);
        }
    }

    public List<IncidentRecord> listIncidents() {
        return incidentRepository.findAll();
    }

    private String cleanJsonResponse(String response) {
        if (response == null || response.isBlank()) {
            throw new IncidentClassificationException("AI provider returned an empty response");
        }
        // Strip markdown code fences if the AI wraps the JSON in ```json ... ```
        String cleaned = response.strip();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceFirst("^```(?:json)?\\s*", "");
            cleaned = cleaned.replaceFirst("\\s*```$", "");
            cleaned = cleaned.strip();
        }
        return cleaned;
    }
}

