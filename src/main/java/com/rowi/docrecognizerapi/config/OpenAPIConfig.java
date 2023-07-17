package com.rowi.docrecognizerapi.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfig {
    private static final String OAUTH_SCHEME_NAME = "oAuth";
  /*  @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.auth-server-url}")
    private String authUrl;
    private static final String TOKEN_URL_FORMAT = "%s/realms/%s/protocol/openid-connect/token";*/
    String url = "https://keycloak.yamakassi.ru/realms/test/protocol/openid-connect/token";

    @Bean
    public OpenAPI myOpenAPI() {
        Info info = new Info()
                .title("Doc Recognizer API");

        return new OpenAPI().info(info).components(new Components()
                .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme())).addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME));
    }

    private SecurityScheme createOAuthScheme( ) {

        OAuthFlows flows = new OAuthFlows().password(new OAuthFlow().tokenUrl(url).scopes(new Scopes()));

        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(flows);
    }

}


