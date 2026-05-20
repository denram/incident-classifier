package br.gov.sctec.incidentclassifier.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentClassificationTest {

    @Test
    void builder_shouldCreateValidInstance() {
        LocalDateTime now = LocalDateTime.now();

        IncidentClassification classification = IncidentClassification.builder()
                .formalizedIncidentText("Texto formalizado.")
                .category(IncidentCategory.SECURITY)
                .severity(IncidentSeverity.HIGH)
                .registeredAt(now)
                .suggestedAction("Ação sugerida.")
                .build();

        assertThat(classification.getFormalizedIncidentText()).isEqualTo("Texto formalizado.");
        assertThat(classification.getCategory()).isEqualTo(IncidentCategory.SECURITY);
        assertThat(classification.getSeverity()).isEqualTo(IncidentSeverity.HIGH);
        assertThat(classification.getRegisteredAt()).isEqualTo(now);
        assertThat(classification.getSuggestedAction()).isEqualTo("Ação sugerida.");
    }

    @ParameterizedTest
    @EnumSource(IncidentCategory.class)
    void category_shouldAcceptAllValues(IncidentCategory category) {
        IncidentClassification classification = IncidentClassification.builder()
                .category(category)
                .build();

        assertThat(classification.getCategory()).isEqualTo(category);
    }

    @ParameterizedTest
    @EnumSource(IncidentSeverity.class)
    void severity_shouldAcceptAllValues(IncidentSeverity severity) {
        IncidentClassification classification = IncidentClassification.builder()
                .severity(severity)
                .build();

        assertThat(classification.getSeverity()).isEqualTo(severity);
    }
}
