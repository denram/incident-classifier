package br.gov.sctec.incidentclassifier.service;

import br.gov.sctec.incidentclassifier.exception.IncidentClassificationException;
import br.gov.sctec.incidentclassifier.model.IncidentClassification;
import br.gov.sctec.incidentclassifier.provider.ApiProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IncidentClassifierService {

    private final ApiProvider apiProvider;
    private final String systemPrompt;
    private final ObjectMapper objectMapper;

    public IncidentClassifierService(
            ApiProvider apiProvider,
            @Qualifier("incidentClassifierSystemPrompt") String systemPrompt,
            ObjectMapper objectMapper) {
        this.apiProvider = apiProvider;
        this.systemPrompt = systemPrompt;
        this.objectMapper = objectMapper;
    }

    public IncidentClassification incidentClassifier(String userPrompt) {
        log.info("Starting incident classification");

        String aiResponse = apiProvider.getResponse(systemPrompt, userPrompt, null);
        String cleanedResponse = cleanJsonResponse(aiResponse);

        try {
            IncidentClassification classification = objectMapper.readValue(cleanedResponse, IncidentClassification.class);
            log.info("Incident classified: category={}, severity={}", classification.getCategory(), classification.getSeverity());
            return classification;
        } catch (JsonProcessingException e) {
            log.error("Failed to parse AI response into IncidentClassification. Response: {}", cleanedResponse, e);
            throw new IncidentClassificationException(
                    "Failed to parse AI response into IncidentClassification: " + e.getMessage(), e);
        }
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
