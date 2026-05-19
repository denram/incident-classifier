package br.gov.sctec.incidentclassifier.provider.anthropic;

import br.gov.sctec.incidentclassifier.model.Tool;
import br.gov.sctec.incidentclassifier.provider.ApiProvider;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "anthropic")
@RequiredArgsConstructor
public class AnthropicApiProvider implements ApiProvider {

    @Value("${ai.anthropic.api-key}")
    private String apiKey;

    @Value("${ai.anthropic.model}")
    private String model;

    @Value("${ai.anthropic.api-url}")
    private String apiUrl;

    @Value("${ai.anthropic.version}")
    private String anthropicVersion;

    @Value("${ai.anthropic.max-tokens}")
    private int maxTokens;

    private final RestTemplate restTemplate;

    @Override
    public String getResponse(String systemPrompt, String userPrompt, List<Tool> tools) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", anthropicVersion);

        Map<String, Object> requestBody = buildRequestBody(systemPrompt, userPrompt, tools);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<JsonNode> response = restTemplate.postForEntity(apiUrl, request, JsonNode.class);

        return extractText(response.getBody());
    }

    private Map<String, Object> buildRequestBody(String systemPrompt, String userPrompt, List<Tool> tools) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("max_tokens", maxTokens);
        body.put("system", systemPrompt);
        body.put("messages", List.of(Map.of("role", "user", "content", userPrompt)));

        if (tools != null && !tools.isEmpty()) {
            body.put("tools", tools.stream().map(tool -> {
                Map<String, Object> t = new HashMap<>();
                t.put("name", tool.getName());
                t.put("description", tool.getDescription());
                t.put("input_schema", tool.getInputSchema());
                return t;
            }).toList());
        }

        return body;
    }

    private String extractText(JsonNode responseBody) {
        return responseBody.path("content").path(0).path("text").asText();
    }
}
