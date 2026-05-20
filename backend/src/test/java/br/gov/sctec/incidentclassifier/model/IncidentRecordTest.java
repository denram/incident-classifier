package br.gov.sctec.incidentclassifier.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentRecordTest {

    @Test
    void builder_shouldCreateValidInstance() {
        LocalDateTime now = LocalDateTime.now();

        IncidentRecord record = IncidentRecord.builder()
                .id(1L)
                .originalText("texto original")
                .formalizedIncidentText("Texto formalizado.")
                .category(IncidentCategory.NETWORK)
                .severity(IncidentSeverity.MEDIUM)
                .registeredAt(now)
                .suggestedAction("Verificar rede.")
                .build();

        assertThat(record.getId()).isEqualTo(1L);
        assertThat(record.getOriginalText()).isEqualTo("texto original");
        assertThat(record.getFormalizedIncidentText()).isEqualTo("Texto formalizado.");
        assertThat(record.getCategory()).isEqualTo(IncidentCategory.NETWORK);
        assertThat(record.getSeverity()).isEqualTo(IncidentSeverity.MEDIUM);
        assertThat(record.getRegisteredAt()).isEqualTo(now);
        assertThat(record.getSuggestedAction()).isEqualTo("Verificar rede.");
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyInstance() {
        IncidentRecord record = new IncidentRecord();

        assertThat(record.getId()).isNull();
        assertThat(record.getOriginalText()).isNull();
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        IncidentRecord record = new IncidentRecord();
        record.setOriginalText("teste");
        record.setCategory(IncidentCategory.SOFTWARE);

        assertThat(record.getOriginalText()).isEqualTo("teste");
        assertThat(record.getCategory()).isEqualTo(IncidentCategory.SOFTWARE);
    }
}
