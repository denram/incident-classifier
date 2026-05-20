package br.gov.sctec.incidentclassifier.service;

import br.gov.sctec.incidentclassifier.exception.IncidentClassificationException;
import br.gov.sctec.incidentclassifier.model.IncidentCategory;
import br.gov.sctec.incidentclassifier.model.IncidentClassification;
import br.gov.sctec.incidentclassifier.model.IncidentRecord;
import br.gov.sctec.incidentclassifier.model.IncidentSeverity;
import br.gov.sctec.incidentclassifier.model.Tool;
import br.gov.sctec.incidentclassifier.provider.ApiProvider;
import br.gov.sctec.incidentclassifier.repository.IncidentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class IncidentClassifierServiceTest {

    @Autowired
    private IncidentRepository incidentRepository;

    private IncidentClassifierService service;
    private StubApiProvider stubApiProvider;

    private final String systemPrompt = "You are an incident classifier.";

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        stubApiProvider = new StubApiProvider();
        service = new IncidentClassifierService(stubApiProvider, systemPrompt, objectMapper, incidentRepository);
        incidentRepository.deleteAll();
    }

    @Test
    void incidentClassifier_withValidResponse_shouldReturnClassification() {
        stubApiProvider.setResponse("""
                {
                    "formalizedIncidentText": "Sistema indisponível desde as 08:00.",
                    "category": "SERVICE_OUTAGE",
                    "severity": "HIGH",
                    "registeredAt": "2025-05-20T10:00:00",
                    "suggestedAction": "Acionar equipe de infraestrutura."
                }
                """);

        IncidentClassification result = service.incidentClassifier("sistema fora do ar");

        assertThat(result).isNotNull();
        assertThat(result.getFormalizedIncidentText()).isEqualTo("Sistema indisponível desde as 08:00.");
        assertThat(result.getCategory()).isEqualTo(IncidentCategory.SERVICE_OUTAGE);
        assertThat(result.getSeverity()).isEqualTo(IncidentSeverity.HIGH);
        assertThat(result.getSuggestedAction()).isEqualTo("Acionar equipe de infraestrutura.");
    }

    @Test
    void incidentClassifier_withMarkdownWrappedResponse_shouldParseCorrectly() {
        stubApiProvider.setResponse("""
                ```json
                {
                    "formalizedIncidentText": "Falha de rede detectada.",
                    "category": "NETWORK",
                    "severity": "MEDIUM",
                    "registeredAt": "2025-05-20T10:00:00",
                    "suggestedAction": "Verificar conectividade."
                }
                ```
                """);

        IncidentClassification result = service.incidentClassifier("rede caiu");

        assertThat(result).isNotNull();
        assertThat(result.getCategory()).isEqualTo(IncidentCategory.NETWORK);
        assertThat(result.getSeverity()).isEqualTo(IncidentSeverity.MEDIUM);
    }

    @Test
    void incidentClassifier_withInvalidJson_shouldThrowException() {
        stubApiProvider.setResponse("invalid json response");

        assertThatThrownBy(() -> service.incidentClassifier("teste"))
                .isInstanceOf(IncidentClassificationException.class)
                .hasMessageContaining("Failed to parse AI response into IncidentClassification");
    }

    @Test
    void incidentClassifier_withEmptyResponse_shouldThrowException() {
        stubApiProvider.setResponse("");

        assertThatThrownBy(() -> service.incidentClassifier("teste"))
                .isInstanceOf(IncidentClassificationException.class)
                .hasMessageContaining("AI provider returned an empty response");
    }

    @Test
    void incidentClassifier_withNullResponse_shouldThrowException() {
        stubApiProvider.setResponse(null);

        assertThatThrownBy(() -> service.incidentClassifier("teste"))
                .isInstanceOf(IncidentClassificationException.class)
                .hasMessageContaining("AI provider returned an empty response");
    }

    @Test
    void incidentClassifier_withBlankResponse_shouldThrowException() {
        stubApiProvider.setResponse("   ");

        assertThatThrownBy(() -> service.incidentClassifier("teste"))
                .isInstanceOf(IncidentClassificationException.class)
                .hasMessageContaining("AI provider returned an empty response");
    }

    @Test
    void incidentClassifier_shouldPersistRecord() {
        stubApiProvider.setResponse("""
                {
                    "formalizedIncidentText": "Incidente registrado.",
                    "category": "SECURITY",
                    "severity": "LOW",
                    "registeredAt": "2025-05-20T10:00:00",
                    "suggestedAction": "Monitorar."
                }
                """);

        service.incidentClassifier("alguem suspeito");

        List<IncidentRecord> records = incidentRepository.findAll();
        assertThat(records).hasSize(1);
        assertThat(records.get(0).getOriginalText()).isEqualTo("alguem suspeito");
        assertThat(records.get(0).getFormalizedIncidentText()).isEqualTo("Incidente registrado.");
        assertThat(records.get(0).getCategory()).isEqualTo(IncidentCategory.SECURITY);
        assertThat(records.get(0).getSeverity()).isEqualTo(IncidentSeverity.LOW);
    }

    @Test
    void listIncidents_shouldReturnAllRecords() {
        IncidentRecord record = IncidentRecord.builder()
                .originalText("incidente")
                .formalizedIncidentText("Incidente formal.")
                .category(IncidentCategory.HARDWARE)
                .severity(IncidentSeverity.MEDIUM)
                .registeredAt(java.time.LocalDateTime.now())
                .suggestedAction("Verificar hardware.")
                .build();
        incidentRepository.save(record);

        List<IncidentRecord> result = service.listIncidents();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOriginalText()).isEqualTo("incidente");
    }

    @Test
    void listIncidents_whenNoRecords_shouldReturnEmptyList() {
        List<IncidentRecord> result = service.listIncidents();

        assertThat(result).isEmpty();
    }

    @Test
    void incidentClassifier_withCodeFenceNoLanguage_shouldParseCorrectly() {
        stubApiProvider.setResponse("""
                ```
                {
                    "formalizedIncidentText": "Problema de compliance.",
                    "category": "COMPLIANCE",
                    "severity": "HIGH",
                    "registeredAt": "2025-05-20T12:00:00",
                    "suggestedAction": "Notificar departamento jurídico."
                }
                ```
                """);

        IncidentClassification result = service.incidentClassifier("violação regulamentar");

        assertThat(result.getCategory()).isEqualTo(IncidentCategory.COMPLIANCE);
    }

    @Test
    void incidentClassifier_shouldPassCorrectPrompts() {
        stubApiProvider.setResponse("""
                {
                    "formalizedIncidentText": "Teste.",
                    "category": "OTHER",
                    "severity": "LOW",
                    "registeredAt": "2025-05-20T10:00:00",
                    "suggestedAction": "Nenhuma."
                }
                """);

        service.incidentClassifier("meu prompt de teste");

        assertThat(stubApiProvider.getLastSystemPrompt()).isEqualTo(systemPrompt);
        assertThat(stubApiProvider.getLastUserPrompt()).isEqualTo("meu prompt de teste");
    }

    private static class StubApiProvider implements ApiProvider {
        private String response;
        private String lastSystemPrompt;
        private String lastUserPrompt;

        void setResponse(String response) {
            this.response = response;
        }

        String getLastSystemPrompt() {
            return lastSystemPrompt;
        }

        String getLastUserPrompt() {
            return lastUserPrompt;
        }

        @Override
        public String getResponse(String systemPrompt, String userPrompt, List<Tool> tools) {
            this.lastSystemPrompt = systemPrompt;
            this.lastUserPrompt = userPrompt;
            return response;
        }
    }
}
