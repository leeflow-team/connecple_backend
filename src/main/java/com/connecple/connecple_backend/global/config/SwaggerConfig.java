package com.connecple.connecple_backend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(title = "API 문서", version = "v1", description = "API 설명입니다.")
)
@Configuration
public class SwaggerConfig {
}
