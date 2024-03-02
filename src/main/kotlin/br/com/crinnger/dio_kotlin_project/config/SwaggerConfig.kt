package br.com.crinnger.dio_kotlin_project.config

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun publicApi(): GroupedOpenApi{
        return GroupedOpenApi.builder()
                .group("creditapi-public")
                .pathsToMatch("/api/customer/**","/api/credits/**")
                .build()
    }
}