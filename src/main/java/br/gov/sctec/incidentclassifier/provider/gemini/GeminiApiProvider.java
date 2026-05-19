package br.gov.sctec.incidentclassifier.provider.gemini;

import br.gov.sctec.incidentclassifier.model.Tool;
import br.gov.sctec.incidentclassifier.provider.ApiProvider;
import com.google.genai.Client;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "gemini")
public class GeminiApiProvider implements ApiProvider {

    @Value("${ai.gemini.api-key}")
    private String apiKey;

    @Value("${ai.gemini.model}")
    private String model;

    @Value("${ai.gemini.max-output-tokens}")
    private int maxOutputTokens;

    private Client client;

    @PostConstruct
    private void init() {
        client = new Client.Builder().apiKey(apiKey).build();
    }

    @Override
    public String getResponse(String systemPrompt, String userPrompt, List<Tool> tools) {
        GenerateContentConfig.Builder configBuilder = GenerateContentConfig.builder()
                .systemInstruction(systemPrompt)
                .maxOutputTokens(maxOutputTokens);

        if (tools != null && !tools.isEmpty()) {
            configBuilder.tools(List.of(buildGeminiTool(tools)));
        }

        GenerateContentResponse response = client.models.generateContent(model, userPrompt, configBuilder.build());

        return response.text();
    }

    private com.google.genai.types.Tool buildGeminiTool(List<Tool> tools) {
        List<FunctionDeclaration> declarations = tools.stream()
                .map(tool -> FunctionDeclaration.builder()
                        .name(tool.getName())
                        .description(tool.getDescription())
                        .parameters(buildSchema(tool.getInputSchema()))
                        .build())
                .toList();

        return com.google.genai.types.Tool.builder()
                .functionDeclarations(declarations)
                .build();
    }

    @SuppressWarnings("unchecked")
    private Schema buildSchema(Map<String, Object> inputSchema) {
        if (inputSchema == null) {
            return Schema.builder().type(Type.Known.OBJECT).build();
        }

        Schema.Builder schemaBuilder = Schema.builder();

        Object type = inputSchema.get("type");
        if (type != null) {
            schemaBuilder.type(mapType(type.toString()));
        }

        Object description = inputSchema.get("description");
        if (description != null) {
            schemaBuilder.description(description.toString());
        }

        Object properties = inputSchema.get("properties");
        if (properties instanceof Map<?, ?> propsMap) {
            Map<String, Schema> schemaProperties = new HashMap<>();
            propsMap.forEach((key, value) -> {
                if (value instanceof Map<?, ?>) {
                    schemaProperties.put(key.toString(), buildSchema((Map<String, Object>) value));
                }
            });
            schemaBuilder.properties(schemaProperties);
        }

        Object required = inputSchema.get("required");
        if (required instanceof List<?> requiredList) {
            schemaBuilder.required(requiredList.stream().map(Object::toString).toList());
        }

        return schemaBuilder.build();
    }

    private Type mapType(String typeName) {
        return switch (typeName.toUpperCase()) {
            case "STRING" -> Type.Known.STRING;
            case "NUMBER" -> Type.Known.NUMBER;
            case "INTEGER" -> Type.Known.INTEGER;
            case "BOOLEAN" -> Type.Known.BOOLEAN;
            case "ARRAY" -> Type.Known.ARRAY;
            default -> Type.Known.OBJECT;
        };
    }
}
