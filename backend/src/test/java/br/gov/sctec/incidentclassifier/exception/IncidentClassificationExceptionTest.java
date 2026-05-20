package br.gov.sctec.incidentclassifier.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentClassificationExceptionTest {

    @Test
    void constructor_withMessage_shouldSetMessage() {
        IncidentClassificationException exception = new IncidentClassificationException("test error");

        assertThat(exception.getMessage()).isEqualTo("test error");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void constructor_withMessageAndCause_shouldSetBoth() {
        RuntimeException cause = new RuntimeException("root cause");
        IncidentClassificationException exception = new IncidentClassificationException("test error", cause);

        assertThat(exception.getMessage()).isEqualTo("test error");
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}
