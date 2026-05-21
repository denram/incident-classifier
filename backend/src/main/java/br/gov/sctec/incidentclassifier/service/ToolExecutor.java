package br.gov.sctec.incidentclassifier.service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ToolExecutor {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public String execute(String toolName) {
        if ("get_current_datetime".equals(toolName)) {
            return LocalDateTime.now().format(FORMATTER);
        }
        throw new IllegalArgumentException("Tool desconhecida: " + toolName);
    }
}
