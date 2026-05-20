package br.gov.sctec.incidentclassifier;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentClassifierApplicationTest {

    @Test
    void mainClass_shouldExist() {
        assertThat(IncidentClassifierApplication.class).isNotNull();
    }
}
