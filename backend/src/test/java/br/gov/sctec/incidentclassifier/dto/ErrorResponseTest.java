package br.gov.sctec.incidentclassifier.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void of_shouldCreateInstanceWithTimestamp() {
        ErrorResponse response = ErrorResponse.of(400, "Bad Request", "Invalid input");

        assertThat(response.status()).isEqualTo(400);
        assertThat(response.error()).isEqualTo("Bad Request");
        assertThat(response.message()).isEqualTo("Invalid input");
        assertThat(response.timestamp()).isNotNull();
        assertThat(response.timestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void of_withDifferentStatus_shouldCreateCorrectInstance() {
        ErrorResponse response = ErrorResponse.of(500, "Internal Server Error", "Something went wrong");

        assertThat(response.status()).isEqualTo(500);
        assertThat(response.error()).isEqualTo("Internal Server Error");
        assertThat(response.message()).isEqualTo("Something went wrong");
    }
}
