package ru.atc.mvd.gismu.shared2.swagger.springboot2.core.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.atc.mvd.gismu.shared2.swagger.springboot2.core.config.OpenApiConfig;
import ru.atc.mvd.gismu.shared2.swagger.springboot2.core.enums.AuthTypes;

import java.util.List;

/**
 * Конфигурация.
 */
@Data
@Component
@ConditionalOnBean(OpenApiConfig.class)
@ConfigurationProperties(prefix = "swagger-autoconfig")
public class SwaggerProperties {

    /** Заголовок. */
    private String openApiTitle = "NO_TITLE";

    /** URI для Swagger. */
    private String uri = "/swagger";

    private AuthConfig authConfig;
    private ServersConfig serversConfig;
    private CorsConfig corsConfig;

    /**
     * Конфиг аутентификации.
     */
    @Data
    public static class AuthConfig {
        private AuthTypes authType = AuthTypes.NONE;
    }

    /**
     * Конфиг серверов.
     */
    @Data
    public static class ServersConfig {
        private boolean useLocal = true;
        private List<Servers> servers;
    }

    /**
     * Серверы.
     */
    @Data
    public static class Servers {
        private String url;
        private String description;
    }

    /**
     * Конфигурация CORS.
     */
    @Data
    public static class CorsConfig {
        private boolean enabled;
    }
}
