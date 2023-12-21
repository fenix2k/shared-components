package ru.atc.mvd.gismu.shared2.swagger.springboot2.webflux.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.atc.mvd.gismu.shared2.swagger.springboot2.core.config.OpenApiConfig;
import ru.atc.mvd.gismu.shared2.swagger.springboot2.core.config.properties.SwaggerProperties;

import java.net.URI;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * Настройка редиректа OpenApi.
 */
@Configuration
@ComponentScan
@Order(0)
@ConditionalOnProperty(value = "swagger-autoconfig.enabled", matchIfMissing = true)
@Import(OpenApiConfig.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class OpenApiWebConfig {

    /**
     * Redirect OpenApi.
     */
    @Bean
    public RouterFunction<ServerResponse> redirectOpenApi(SwaggerProperties swaggerProperties) {
        return route(GET(swaggerProperties.getUri()), req ->
                ServerResponse.temporaryRedirect(URI.create("/webjars/swagger-ui/index.html"))
                        .build());
    }

    /**
     * Фильтр CORS.
     *
     * @return {@link CorsWebFilter}
     */
    @Bean
    @ConditionalOnProperty(value = "swagger-autoconfig.cors-config.enabled")
    public CorsWebFilter corsFilter() {
        return new CorsWebFilter(corsConfigurationSource());
    }

    private CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.addAllowedMethod(HttpMethod.GET);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.PATCH);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.addAllowedMethod(HttpMethod.OPTIONS);

        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
