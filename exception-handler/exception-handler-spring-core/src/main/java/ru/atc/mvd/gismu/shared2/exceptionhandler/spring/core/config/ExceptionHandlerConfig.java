package ru.atc.mvd.gismu.shared2.exceptionhandler.spring.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers.ExceptionHandler;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers.ExceptionHandlerManager;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.mapper.DefaultErrorMessageMapper;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.mapper.ErrorMessageMapper;
import ru.atc.mvd.gismu.shared2.exceptionhandler.spring.core.config.properties.ExceptionHandlerProperties;

import java.util.List;

/**
 * Конфигуратор глобального обработчика исключений.
 */
@Configuration
@ComponentScan
@ConditionalOnProperty(value = "exception-handler.enabled")
public class ExceptionHandlerConfig {

    /**
     * Бин ErrorMessageMapper.
     *
     * @return {@link ErrorMessageMapper}
     */
    @Bean("errorExceptionHandlers")
    public ErrorMessageMapper errorMessageMapper(ExceptionHandlerProperties properties) {
        return new DefaultErrorMessageMapper(properties);
    }

    /**
     * Бин списка доступных обработчиков ошибок.
     *
     * @param exceptionHandlers {@link List}<{@link ExceptionHandler}>
     * @param errorMessageMapper {@link ErrorMessageMapper}
     * @return {@link ExceptionHandlerManager}
     */
    @Bean("exceptionHandlerManager")
    public ExceptionHandlerManager exceptionHandlerManager(List<ExceptionHandler> exceptionHandlers,
                                                           ErrorMessageMapper errorMessageMapper) {
        return new ExceptionHandlerManager(exceptionHandlers, errorMessageMapper);
    }
}
