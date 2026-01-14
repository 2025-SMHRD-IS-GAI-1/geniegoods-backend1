package com.example.geniegoods.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // 1. 보안 요구사항 정의 (이름은 자유롭게 지정, 여기선 "cookieAuth")
        String securitySchemeName = "cookieAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(securitySchemeName);

        // 2. 보안 스키마 정의 (쿠키 방식)
        Components components = new Components()
                .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name("accessToken") // 쿠키의 키 이름 (현재 코드상 "accessToken")
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.COOKIE));

        return new OpenAPI()
                .info(new Info()
                        .title("지니굿즈 API 명세서")
                        .description("Spring Boot 4 기반 API 문서입니다.")
                        .version("1.0.0"))
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}
