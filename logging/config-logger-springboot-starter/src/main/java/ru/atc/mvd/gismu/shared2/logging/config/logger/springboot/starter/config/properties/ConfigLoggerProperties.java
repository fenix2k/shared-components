package ru.atc.mvd.gismu.shared2.logging.config.logger.springboot.starter.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.atc.mvd.gismu.shared2.logging.config.logger.springboot.starter.config.ConfigLoggerConfig;

/**
 * Конфигурация.
 */
@Data
@Component
@ConditionalOnBean(ConfigLoggerConfig.class)
@ConfigurationProperties(prefix = "logger.config-logger")
public class ConfigLoggerProperties {

    /** Отключить печать временной зоны. */
    private boolean disableTimeZonePrint;

    /** Отключить печать конфигурации. */
    private boolean disableEnvVarPrint;

    /** Отключить печать бинов. */
    private boolean disableBeanPrint;
}
