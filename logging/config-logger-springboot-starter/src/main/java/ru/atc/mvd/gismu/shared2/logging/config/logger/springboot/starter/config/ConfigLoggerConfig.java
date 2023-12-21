package ru.atc.mvd.gismu.shared2.logging.config.logger.springboot.starter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.AbstractEnvironment;
import ru.atc.mvd.gismu.shared2.logging.config.logger.springboot.starter.config.properties.ConfigLoggerProperties;

/**
 * OpenApi configuration.
 */
@Slf4j
@Configuration
@Order(0)
@ConditionalOnProperty(value = "logger.config-logger.enabled", matchIfMissing = true)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class ConfigLoggerConfig {

    /**
     * Bean ConfigLogger.
     *
     * @param configProps {@link ConfigLoggerProperties}
     * @param environment {@link AbstractEnvironment}
     * @return {@link ConfigLogger}
     */
    @Bean
    @Order
    public ConfigLogger getConfigLogger(ConfigLoggerProperties configProps, AbstractEnvironment environment) {
        return new ConfigLogger(configProps, environment);
    }
}
