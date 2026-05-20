package br.gov.sctec.incidentclassifier.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentSeverityTest {

    @Test
    void shouldHave3Severities() {
        assertThat(IncidentSeverity.values()).hasSize(3);
    }

    @Test
    void shouldContainExpectedValues() {
        assertThat(IncidentSeverity.valueOf("LOW")).isEqualTo(IncidentSeverity.LOW);
        assertThat(IncidentSeverity.valueOf("MEDIUM")).isEqualTo(IncidentSeverity.MEDIUM);
        assertThat(IncidentSeverity.valueOf("HIGH")).isEqualTo(IncidentSeverity.HIGH);
    }
}
