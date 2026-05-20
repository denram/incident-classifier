package br.gov.sctec.incidentclassifier.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IncidentRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void validRequest_shouldHaveNoViolations() {
        IncidentRequest request = new IncidentRequest("Sistema fora do ar");

        var violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void blankIncident_shouldHaveViolation() {
        IncidentRequest request = new IncidentRequest("");

        var violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Incident description must not be blank");
    }

    @Test
    void nullIncident_shouldHaveViolation() {
        IncidentRequest request = new IncidentRequest(null);

        var violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
    }

    @Test
    void gettersAndSetters_shouldWork() {
        IncidentRequest request = new IncidentRequest();
        request.setIncident("teste");

        assertThat(request.getIncident()).isEqualTo("teste");
    }
}
