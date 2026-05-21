package br.gov.sctec.incidentclassifier.config;

import br.gov.sctec.incidentclassifier.model.Tool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class ToolDefinitions {

    @Bean
    public List<Tool> classificationTools() {
        Tool getCurrentDateTime = Tool.builder()
                .name("get_current_datetime")
                .description("Obtém a data e hora atuais do servidor da aplicação no formato ISO 8601 (yyyy-MM-ddTHH:mm:ss). "
                        + "Use esta ferramenta para preencher o campo registeredAt da classificação de incidentes.")
                .inputSchema(Map.of(
                        "type", "object",
                        "properties", Map.of(),
                        "required", List.of()
                ))
                .build();

        return List.of(getCurrentDateTime);
    }
}
