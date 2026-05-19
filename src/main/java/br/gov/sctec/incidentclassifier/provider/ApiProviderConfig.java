package br.gov.sctec.incidentclassifier.provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:config.properties")
public class ApiProviderConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
