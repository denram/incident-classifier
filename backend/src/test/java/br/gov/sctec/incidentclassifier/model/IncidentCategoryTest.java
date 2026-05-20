package br.gov.sctec.incidentclassifier.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentCategoryTest {

    @Test
    void shouldHave13Categories() {
        assertThat(IncidentCategory.values()).hasSize(13);
    }

    @Test
    void shouldContainExpectedValues() {
        assertThat(IncidentCategory.valueOf("SECURITY")).isEqualTo(IncidentCategory.SECURITY);
        assertThat(IncidentCategory.valueOf("NETWORK")).isEqualTo(IncidentCategory.NETWORK);
        assertThat(IncidentCategory.valueOf("HARDWARE")).isEqualTo(IncidentCategory.HARDWARE);
        assertThat(IncidentCategory.valueOf("SOFTWARE")).isEqualTo(IncidentCategory.SOFTWARE);
        assertThat(IncidentCategory.valueOf("DATABASE")).isEqualTo(IncidentCategory.DATABASE);
        assertThat(IncidentCategory.valueOf("DATA_LOSS")).isEqualTo(IncidentCategory.DATA_LOSS);
        assertThat(IncidentCategory.valueOf("ACCESS_CONTROL")).isEqualTo(IncidentCategory.ACCESS_CONTROL);
        assertThat(IncidentCategory.valueOf("PERFORMANCE")).isEqualTo(IncidentCategory.PERFORMANCE);
        assertThat(IncidentCategory.valueOf("SERVICE_OUTAGE")).isEqualTo(IncidentCategory.SERVICE_OUTAGE);
        assertThat(IncidentCategory.valueOf("INFRASTRUCTURE")).isEqualTo(IncidentCategory.INFRASTRUCTURE);
        assertThat(IncidentCategory.valueOf("COMMUNICATION")).isEqualTo(IncidentCategory.COMMUNICATION);
        assertThat(IncidentCategory.valueOf("COMPLIANCE")).isEqualTo(IncidentCategory.COMPLIANCE);
        assertThat(IncidentCategory.valueOf("OTHER")).isEqualTo(IncidentCategory.OTHER);
    }
}
