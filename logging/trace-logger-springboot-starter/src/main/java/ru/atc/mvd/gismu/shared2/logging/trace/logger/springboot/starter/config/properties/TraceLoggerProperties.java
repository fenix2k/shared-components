package ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.config.TraceLoggerConfig;

/**
 * Параметры конфигурации.
 */
@Getter
@NoArgsConstructor
@ConfigurationProperties(prefix = "logging.trace-logger")
@Component
@ConditionalOnBean(TraceLoggerConfig.class)
public class TraceLoggerProperties {

    /** Флаг, выводить ли имя класс в сокращенном виде. */
    private boolean showShortClassName = true;

    /** Флаг, выводить ли возвращаемое значение. */
    private boolean showReturnValue = true;
}
