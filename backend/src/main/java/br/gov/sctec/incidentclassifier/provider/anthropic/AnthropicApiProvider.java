package br.gov.sctec.incidentclassifier.provider.anthropic;

import br.gov.sctec.incidentclassifier.model.Tool;
import br.gov.sctec.incidentclassifier.provider.ApiProvider;
import br.gov.sctec.incidentclassifier.service.ToolExecutor;
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

import java.util.ArrayList;
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
    private final ToolExecutor toolExecutor;

    @Override
    public String getResponse(String systemPrompt, String userPrompt, List<Tool> tools) {
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", userPrompt));

        JsonNode responseBody = sendRequest(systemPrompt, messages, tools);
        String stopReason = responseBody.path("stop_reason").asText();

        if ("tool_use".equals(stopReason)) {
            messages.add(buildAssistantMessage(responseBody));
            messages.add(buildToolResultMessage(responseBody));

            JsonNode finalResponse = sendRequest(systemPrompt, messages, tools);
            return extractText(finalResponse);
        }

        return extractText(responseBody);
    }

    private JsonNode sendRequest(String systemPrompt, List<Map<String, Object>> messages, List<Tool> tools) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", anthropicVersion);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("max_tokens", maxTokens);
        body.put("system", systemPrompt);
        body.put("messages", messages);

        if (tools != null && !tools.isEmpty()) {
            body.put("tools", tools.stream().map(tool -> {
                Map<String, Object> t = new HashMap<>();
                t.put("name", tool.getName());
                t.put("description", tool.getDescription());
                t.put("input_schema", tool.getInputSchema());
                return t;
            }).toList());
        }

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<JsonNode> response = restTemplate.postForEntity(apiUrl, request, JsonNode.class);
        return response.getBody();
    }

    private Map<String, Object> buildAssistantMessage(JsonNode responseBody) {
        List<Map<String, Object>> contentBlocks = new ArrayList<>();
        for (JsonNode block : responseBody.path("content")) {
            Map<String, Object> blockMap = new HashMap<>();
            blockMap.put("type", block.path("type").asText());
            if ("text".equals(block.path("type").asText())) {
                blockMap.put("text", block.path("text").asText());
            } else if ("tool_use".equals(block.path("type").asText())) {
                blockMap.put("id", block.path("id").asText());
                blockMap.put("name", block.path("name").asText());
                blockMap.put("input", Map.of());
            }
            contentBlocks.add(blockMap);
        }
        return Map.of("role", "assistant", "content", contentBlocks);
    }

    private Map<String, Object> buildToolResultMessage(JsonNode responseBody) {
        List<Map<String, Object>> toolResults = new ArrayList<>();
        for (JsonNode block : responseBody.path("content")) {
            if ("tool_use".equals(block.path("type").asText())) {
                String toolName = block.path("name").asText();
                String toolUseId = block.path("id").asText();
                String result = toolExecutor.execute(toolName);
                toolResults.add(Map.of(
                        "type", "tool_result",
                        "tool_use_id", toolUseId,
                        "content", result
                ));
            }
        }
        return Map.of("role", "user", "content", toolResults);
    }

    private String extractText(JsonNode responseBody) {
        for (JsonNode block : responseBody.path("content")) {
            if ("text".equals(block.path("type").asText())) {
                return block.path("text").asText();
            }
        }
        return "";
    }
}
