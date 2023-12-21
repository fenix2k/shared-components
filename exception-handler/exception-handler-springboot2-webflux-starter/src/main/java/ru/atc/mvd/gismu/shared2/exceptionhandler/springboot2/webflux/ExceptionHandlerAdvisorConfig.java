package ru.atc.mvd.gismu.shared2.exceptionhandler.springboot2.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers.ExceptionHandlerManager;
import ru.atc.mvd.gismu.shared2.exceptionhandler.spring.core.config.ExceptionHandlerConfig;

/**
 * Глобальный перехватчик исключений.
 */
@ControllerAdvice
@Configuration
@ConditionalOnProperty(value = "exception-handler.enabled", matchIfMissing = true)
@Import(ExceptionHandlerConfig.class)
@Slf4j
public class ExceptionHandlerAdvisorConfig extends ExceptionHandlerControllerAdviser {

    public ExceptionHandlerAdvisorConfig(ExceptionHandlerManager exceptionHandlerManager) {
        super(exceptionHandlerManager);
        log.info("ExceptionHandler is enabled");
    }
}
