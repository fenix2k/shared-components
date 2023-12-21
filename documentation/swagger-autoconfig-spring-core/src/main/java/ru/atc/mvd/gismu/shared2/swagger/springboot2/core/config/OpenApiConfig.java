package ru.atc.mvd.gismu.shared2.swagger.springboot2.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import ru.atc.mvd.gismu.shared2.swagger.springboot2.core.config.properties.SwaggerProperties;
import ru.atc.mvd.gismu.shared2.swagger.springboot2.core.enums.AuthTypes;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * OpenApi configuration.
 */
@Configuration
@Order(0)
@ConditionalOnProperty(value = "swagger-autoconfig.enabled")
@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class OpenApiConfig {

    /**
     * Bean BuildProperties.
     *
     * @param entries {@link Properties}
     * @return {@link BuildProperties}
     */
    @Bean
    @Order
    public BuildProperties buildProperties(Properties entries) {
        return new BuildProperties(entries);
    }

    /**
     * Конфигурация SpringDoc.
     *
     * @return {@link SpringDocUtils}
     */
    @Bean
    @Order(0)
    public SpringDocUtils getDeafultSpringDocUtils() {
        SpringDocUtils springDocUtils = SpringDocUtils.getConfig();

        // Формат отображения LocalDateTime
        Schema<LocalDateTime> schemaLocalDateTime = new Schema<>();
        schemaLocalDateTime.example(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).type("string");
        springDocUtils.replaceWithSchema(LocalDateTime.class, schemaLocalDateTime);
        // Формат отображения OffsetDateTime
        Schema<OffsetDateTime> schemaOffsetDateTime = new Schema<>();
        schemaOffsetDateTime.example(OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)).type("string");
        springDocUtils.replaceWithSchema(OffsetDateTime.class, schemaOffsetDateTime);

        return springDocUtils;
    }

    /**
     * OpenAPI.
     *
     * @param buildProperties {@link BuildProperties}
     * @param swaggerProperties конфигурация application.properties {@link SwaggerProperties}
     * @return {@link OpenAPI}
     */
    @Bean
    @Order(0)
    public OpenAPI getOpenAPI(BuildProperties buildProperties, SwaggerProperties swaggerProperties) {
        Info info = new Info()
                .title(swaggerProperties.getOpenApiTitle())
                .version(buildProperties.getVersion());

        log.info("OpenApi is enabled. Configuration: {}", swaggerProperties);

        OpenAPI openAPI = new OpenAPI().info(info);

        if (swaggerProperties.getAuthConfig() != null) {
            AuthTypes authType = swaggerProperties.getAuthConfig().getAuthType();
            if (authType != null) {
                if (authType == AuthTypes.BASIC) {
                    setAuthTypeBasic(openAPI);
                } else if (authType == AuthTypes.BEARER) {
                    setAuthTypeBearer(openAPI);
                }
            }
        }

        if (swaggerProperties.getServersConfig() != null) {
            setServers(openAPI, swaggerProperties.getServersConfig());
        }

        return openAPI;
    }

    /**
     * Установить аутентификацию по логину и паролю.
     *
     * @param openAPI {@link OpenAPI}
     */
    private void setAuthTypeBasic(OpenAPI openAPI) {
        final String securitySchemeName = "BasicAuth";
        final String securitySchemeType = "basic";
        openAPI
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme(securitySchemeType)
                                )
                );
    }

    /**
     * Установить аутентификацию по токену.
     *
     * @param openAPI {@link OpenAPI}
     */
    private void setAuthTypeBearer(OpenAPI openAPI) {
        final String securitySchemeName = "BearerAuth";
        final String securitySchemeType = "bearer";
        final String bearerFormat = "JWT";
        openAPI
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme(securitySchemeType)
                                                .bearerFormat(bearerFormat)
                                )
                );
    }

    /**
     * Установить список серверов.
     *
     * @param openAPI {@link OpenAPI}
     * @param serversConfig {@link SwaggerProperties.ServersConfig}
     */
    private void setServers(OpenAPI openAPI, SwaggerProperties.ServersConfig serversConfig) {
        // Список URL
        List<Server> servers = new ArrayList<>();
        if (serversConfig.isUseLocal()) {
            servers.add(new Server().url("/").description("Local server"));
        }
        if (serversConfig.getServers() != null) {
            for (SwaggerProperties.Servers server : serversConfig.getServers()) {
                servers.add(new Server()
                        .url(server.getUrl())
                        .description(server.getDescription()));
            }
        }

        openAPI.setServers(servers);
    }
}
