package com.finance.transaction_service.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Parameter sessionParameter = new Parameter()
                .in("header")
                .name("SESSION")
                .description("세션 ID")
                .required(true)
                .schema(new io.swagger.v3.oas.models.media.StringSchema());

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Session", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("SESSION")))
                .addSecurityItem(new io.swagger.v3.oas.models.security.SecurityRequirement().addList("Session"))
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("API Test")
                .description("Let's practice Swagger UI - Session 인증이 필요한 API입니다.")
                .version("1.0.0");
    }
}