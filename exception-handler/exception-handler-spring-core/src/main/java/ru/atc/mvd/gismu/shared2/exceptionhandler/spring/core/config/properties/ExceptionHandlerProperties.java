package ru.atc.mvd.gismu.shared2.exceptionhandler.spring.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.config.properties.CommonExceptionHandlerProperties;
import ru.atc.mvd.gismu.shared2.exceptionhandler.spring.core.config.ExceptionHandlerConfig;

/**
 * Конфигурация.
 */
@Getter
@Setter
@ToString
@Component
@ConditionalOnBean(ExceptionHandlerConfig.class)
@ConfigurationProperties(prefix = "exception-handler")
public class ExceptionHandlerProperties extends CommonExceptionHandlerProperties {
}
