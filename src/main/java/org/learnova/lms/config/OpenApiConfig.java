package org.learnova.lms.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Learnova API Documentation",
                version = "1.0",
                description = "API documentation for Learnova LMS"
        )
)
public class OpenApiConfig {
}
