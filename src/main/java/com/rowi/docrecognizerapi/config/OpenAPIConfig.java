package com.rowi.docrecognizerapi.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Info info = new Info()
                .title("Doc Recognizer API");

        return new OpenAPI().info(info);
    }
}
