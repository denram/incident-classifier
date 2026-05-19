package br.gov.sctec.incidentclassifier.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
public class SystemPromptConfig {

    @Value("classpath:prompts/incident-classifier-system-prompt.txt")
    private Resource systemPromptResource;

    @Bean(name = "incidentClassifierSystemPrompt")
    public String incidentClassifierSystemPrompt() throws IOException {
        return systemPromptResource.getContentAsString(StandardCharsets.UTF_8);
    }
}
