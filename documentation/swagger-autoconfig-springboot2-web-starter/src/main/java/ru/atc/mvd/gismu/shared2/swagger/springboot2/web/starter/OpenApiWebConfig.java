package ru.atc.mvd.gismu.shared2.swagger.springboot2.web.starter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.atc.mvd.gismu.shared2.swagger.springboot2.core.config.OpenApiConfig;
import ru.atc.mvd.gismu.shared2.swagger.springboot2.core.config.properties.SwaggerProperties;

/**
 * Настройка редиректа OpenApi.
 */
@Configuration
@Order(0)
@ConditionalOnProperty(value = "swagger-autoconfig.enabled", matchIfMissing = true)
@Import(OpenApiConfig.class)
@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class OpenApiWebConfig implements WebMvcConfigurer {

    private static final String DEFAULT_URI = "/swagger-ui/index.html";
    private final SwaggerProperties swaggerProperties;

    public OpenApiWebConfig(SwaggerProperties swaggerProperties) {
        this.swaggerProperties = swaggerProperties;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController(swaggerProperties.getUri(), DEFAULT_URI);
        log.info("OpenApi URI [{}]", swaggerProperties.getUri());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (swaggerProperties.getCorsConfig() != null && swaggerProperties.getCorsConfig().isEnabled()) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedHeaders("*")
                    .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS");
        }
    }
}
