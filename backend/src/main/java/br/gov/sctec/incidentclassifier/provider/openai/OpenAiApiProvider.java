package br.gov.sctec.incidentclassifier.provider.openai;

import br.gov.sctec.incidentclassifier.model.Tool;
import br.gov.sctec.incidentclassifier.provider.ApiProvider;
import br.gov.sctec.incidentclassifier.service.ToolExecutor;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.JsonValue;
import com.openai.models.ChatModel;
import com.openai.models.FunctionDefinition;
import com.openai.models.FunctionParameters;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionFunctionTool;
import com.openai.models.chat.completions.ChatCompletionMessage;
import com.openai.models.chat.completions.ChatCompletionMessageToolCall;
import com.openai.models.chat.completions.ChatCompletionTool;
import com.openai.models.chat.completions.ChatCompletionToolMessageParam;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@ConditionalOnProperty(name = "ai.provider", havingValue = "openai")
@RequiredArgsConstructor
public class OpenAiApiProvider implements ApiProvider {

    @Value("${ai.openai.api-key}")
    private String apiKey;

    @Value("${ai.openai.model}")
    private String model;

    @Value("${ai.openai.max-tokens}")
    private int maxTokens;

    private final ToolExecutor toolExecutor;
    private OpenAIClient client;

    @PostConstruct
    private void init() {
        client = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .build();
    }

    @Override
    public String getResponse(String systemPrompt, String userPrompt, List<Tool> tools) {
        ChatCompletionCreateParams.Builder paramsBuilder = ChatCompletionCreateParams.builder()
                .model(ChatModel.of(model))
                .maxCompletionTokens(maxTokens)
                .addSystemMessage(systemPrompt)
                .addUserMessage(userPrompt);

        if (tools != null && !tools.isEmpty()) {
            tools.forEach(tool -> paramsBuilder.addTool(buildTool(tool)));
        }

        ChatCompletion completion = client.chat().completions().create(paramsBuilder.build());
        ChatCompletionMessage message = completion.choices().get(0).message();

        List<ChatCompletionMessageToolCall> toolCalls = message.toolCalls()
                .stream().flatMap(Collection::stream).toList();

        if (!toolCalls.isEmpty()) {
            paramsBuilder.addMessage(message);
            for (ChatCompletionMessageToolCall toolCall : toolCalls) {
                String toolName = toolCall.asFunction().function().name();
                String toolCallId = toolCall.asFunction().id();
                String result = toolExecutor.execute(toolName);
                paramsBuilder.addMessage(ChatCompletionToolMessageParam.builder()
                        .toolCallId(toolCallId)
                        .content(result)
                        .build());
            }
            ChatCompletion finalCompletion = client.chat().completions().create(paramsBuilder.build());
            return finalCompletion.choices().get(0).message().content().orElse("");
        }

        return message.content().orElse("");
    }

    private ChatCompletionTool buildTool(Tool tool) {
        FunctionParameters.Builder parametersBuilder = FunctionParameters.builder();
        if (tool.getInputSchema() != null) {
            tool.getInputSchema().forEach((key, value) ->
                    parametersBuilder.putAdditionalProperty(key, JsonValue.from(value)));
        }

        ChatCompletionFunctionTool functionTool = ChatCompletionFunctionTool.builder()
                .function(FunctionDefinition.builder()
                        .name(tool.getName())
                        .description(tool.getDescription())
                        .parameters(parametersBuilder.build())
                        .build())
                .build();

        return ChatCompletionTool.ofFunction(functionTool);
    }
}
