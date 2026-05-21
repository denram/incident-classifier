package br.gov.sctec.incidentclassifier.provider.gemini;

import br.gov.sctec.incidentclassifier.model.Tool;
import br.gov.sctec.incidentclassifier.provider.ApiProvider;
import br.gov.sctec.incidentclassifier.service.ToolExecutor;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.FunctionCall;
import com.google.genai.types.FunctionDeclaration;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "gemini")
@RequiredArgsConstructor
public class GeminiApiProvider implements ApiProvider {

    @Value("${ai.gemini.api-key}")
    private String apiKey;

    @Value("${ai.gemini.model}")
    private String model;

    @Value("${ai.gemini.max-output-tokens}")
    private int maxOutputTokens;

    private final ToolExecutor toolExecutor;
    private Client client;

    @PostConstruct
    private void init() {
        client = new Client.Builder().apiKey(apiKey).build();
    }

    @Override
    public String getResponse(String systemPrompt, String userPrompt, List<Tool> tools) {
        Content systemInstruction = Content.fromParts(Part.fromText(systemPrompt));

        GenerateContentConfig.Builder configBuilder = GenerateContentConfig.builder()
                .systemInstruction(systemInstruction)
                .maxOutputTokens(maxOutputTokens);

        if (tools != null && !tools.isEmpty()) {
            configBuilder.tools(List.of(buildGeminiTool(tools)));
        }

        List<Content> contents = new ArrayList<>();
        contents.add(Content.fromParts(Part.fromText(userPrompt)));

        GenerateContentResponse response = client.models.generateContent(model, contents, configBuilder.build());

        List<FunctionCall> functionCalls = response.functionCalls();
        if (functionCalls != null && !functionCalls.isEmpty()) {
            FunctionCall call = functionCalls.get(0);
            String toolName = call.name().orElse("");
            String result = toolExecutor.execute(toolName);

            Content modelContent = response.candidates().orElse(List.of()).get(0).content().orElseThrow();
            contents.add(modelContent);
            contents.add(Content.fromParts(Part.fromFunctionResponse(toolName, Map.of("result", result))));

            GenerateContentResponse finalResponse = client.models.generateContent(model, contents, configBuilder.build());
            return finalResponse.text();
        }

        return response.text();
    }

    private com.google.genai.types.Tool buildGeminiTool(List<Tool> tools) {
        List<FunctionDeclaration> declarations = new ArrayList<>();
        for (Tool tool : tools) {
            declarations.add(FunctionDeclaration.builder()
                    .name(tool.getName())
                    .description(tool.getDescription())
                    .parameters(buildSchema(tool.getInputSchema()))
                    .build());
        }

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

    private Type.Known mapType(String typeName) {
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
