package br.gov.sctec.incidentclassifier.controller;

import br.gov.sctec.incidentclassifier.exception.GlobalExceptionHandler;
import br.gov.sctec.incidentclassifier.exception.IncidentClassificationException;
import br.gov.sctec.incidentclassifier.model.IncidentCategory;
import br.gov.sctec.incidentclassifier.model.IncidentClassification;
import br.gov.sctec.incidentclassifier.model.IncidentRecord;
import br.gov.sctec.incidentclassifier.model.IncidentSeverity;
import br.gov.sctec.incidentclassifier.service.IncidentClassifierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IncidentClassifierControllerTest {

    private MockMvc mockMvc;
    private StubIncidentClassifierService stubService;

    @BeforeEach
    void setUp() {
        stubService = new StubIncidentClassifierService();
        IncidentClassifierController controller = new IncidentClassifierController(stubService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    void classifyIncident_withValidRequest_shouldReturnClassification() throws Exception {
        stubService.setClassification(IncidentClassification.builder()
                .formalizedIncidentText("Sistema indisponível desde as 08:00 para todos os usuários.")
                .category(IncidentCategory.SERVICE_OUTAGE)
                .severity(IncidentSeverity.HIGH)
                .registeredAt(LocalDateTime.of(2025, 5, 20, 10, 0))
                .suggestedAction("Acionar equipe de infraestrutura imediatamente.")
                .build());

        mockMvc.perform(post("/incidentClassifier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"incident\":\"Sistema indisponível para todos os usuários desde as 08:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.formalizedIncidentText").value("Sistema indisponível desde as 08:00 para todos os usuários."))
                .andExpect(jsonPath("$.category").value("SERVICE_OUTAGE"))
                .andExpect(jsonPath("$.severity").value("HIGH"))
                .andExpect(jsonPath("$.suggestedAction").value("Acionar equipe de infraestrutura imediatamente."));
    }

    @Test
    void classifyIncident_whenServiceThrowsClassificationException_shouldReturnUnprocessableEntity() throws Exception {
        stubService.setException(new IncidentClassificationException("Failed to parse AI response"));

        mockMvc.perform(post("/incidentClassifier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"incident\":\"teste de incidente\"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.error").value("Incident Classification Error"))
                .andExpect(jsonPath("$.message").value("Failed to parse AI response"));
    }

    @Test
    void classifyIncident_whenServiceThrowsGenericException_shouldReturnInternalServerError() throws Exception {
        stubService.setException(new RuntimeException("Unexpected error"));

        mockMvc.perform(post("/incidentClassifier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"incident\":\"teste de incidente\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }

    @Test
    void listIncidents_shouldReturnEmptyList() throws Exception {
        stubService.setRecords(Collections.emptyList());

        mockMvc.perform(get("/incidentClassifier"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void listIncidents_shouldReturnListOfRecords() throws Exception {
        IncidentRecord record = IncidentRecord.builder()
                .id(1L)
                .originalText("sistema fora do ar")
                .formalizedIncidentText("Sistema indisponível.")
                .category(IncidentCategory.SERVICE_OUTAGE)
                .severity(IncidentSeverity.HIGH)
                .registeredAt(LocalDateTime.of(2025, 5, 20, 10, 0))
                .suggestedAction("Acionar equipe.")
                .build();

        stubService.setRecords(List.of(record));

        mockMvc.perform(get("/incidentClassifier"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].originalText").value("sistema fora do ar"))
                .andExpect(jsonPath("$[0].category").value("SERVICE_OUTAGE"))
                .andExpect(jsonPath("$[0].severity").value("HIGH"));
    }

    @Test
    void classifyIncident_shouldPassIncidentTextToService() throws Exception {
        stubService.setClassification(IncidentClassification.builder()
                .formalizedIncidentText("Texto.")
                .category(IncidentCategory.OTHER)
                .severity(IncidentSeverity.LOW)
                .registeredAt(LocalDateTime.now())
                .suggestedAction("Nenhuma.")
                .build());

        mockMvc.perform(post("/incidentClassifier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"incident\":\"meu incidente\"}"))
                .andExpect(status().isOk());

        assert stubService.getLastIncidentText().equals("meu incidente");
    }

    private static class StubIncidentClassifierService extends IncidentClassifierService {
        private IncidentClassification classification;
        private List<IncidentRecord> records = Collections.emptyList();
        private RuntimeException exception;
        private String lastIncidentText;

        StubIncidentClassifierService() {
            super(null, null, null, null);
        }

        void setClassification(IncidentClassification classification) {
            this.classification = classification;
            this.exception = null;
        }

        void setRecords(List<IncidentRecord> records) {
            this.records = records;
        }

        void setException(RuntimeException exception) {
            this.exception = exception;
            this.classification = null;
        }

        String getLastIncidentText() {
            return lastIncidentText;
        }

        @Override
        public IncidentClassification incidentClassifier(String userPrompt) {
            this.lastIncidentText = userPrompt;
            if (exception != null) {
                throw exception;
            }
            return classification;
        }

        @Override
        public List<IncidentRecord> listIncidents() {
            return records;
        }
    }
}
