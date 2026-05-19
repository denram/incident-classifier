package br.gov.sctec.incidentclassifier.exception;

public class IncidentClassificationException extends RuntimeException {

    public IncidentClassificationException(String message) {
        super(message);
    }

    public IncidentClassificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
