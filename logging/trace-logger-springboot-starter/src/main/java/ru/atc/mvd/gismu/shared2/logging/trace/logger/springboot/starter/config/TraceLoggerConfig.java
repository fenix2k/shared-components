package ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.aspect.TraceLoggerAspect;
import ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.config.properties.TraceLoggerProperties;

/**
 * Конфигурация.
 */
@Configuration
@ConditionalOnProperty("logging.trace-logger.enabled")
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class TraceLoggerConfig {

    /**
     * TraceLoggerAspect.
     */
    @Bean
    public TraceLoggerAspect getTraceLoggerAspect(TraceLoggerProperties configProps) {
        TraceLoggerAspect traceLoggerAspect = new TraceLoggerAspect(configProps);

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger classicLogger = loggerContext.getLogger(traceLoggerAspect.getClass());
        classicLogger.setLevel(Level.TRACE);

        return traceLoggerAspect;
    }
}
