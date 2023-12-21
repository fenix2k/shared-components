package ru.atc.mvd.gismu.shared2.logging.config.logger.springboot.starter.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.PropertySource;
import ru.atc.mvd.gismu.shared2.logging.config.logger.springboot.starter.config.properties.ConfigLoggerProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Выводит информацию о конфигурации при старте.
 */
@Slf4j
@RequiredArgsConstructor
public class ConfigLogger implements ApplicationListener<ContextRefreshedEvent> {

    private final ConfigLoggerProperties configProps;
    private final AbstractEnvironment environment;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!configProps.isDisableTimeZonePrint()) {
            final TimeZone tz = Calendar.getInstance().getTimeZone();
            log.info("*** TIMEZONE: [{}] {} ***", tz.getID(), tz.getDisplayName());
        }
        if (!configProps.isDisableEnvVarPrint()) {
            printEnvironmentVariables();
        }
        if (!configProps.isDisableBeanPrint()) {
            printBeans(event.getApplicationContext());
        }

        log.info("*******************************");
        log.info("***** Application started *****");
        log.info("*******************************");
    }

    /**
     * Печатает список инициализированных бинов.
     *
     * @param ctx {@link ApplicationContext}
     */
    private void printBeans(ApplicationContext ctx) {
        log.info("For inspect the beans provided by Spring Boot toggle logger debug mode:");
        if (log.isDebugEnabled()) {
            final String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (final String beanName : beanNames) {
                log.debug("BeanName {}", beanName);
            }
        }
    }

    /**
     * Печатает конфигурацию.
     */
    private void printEnvironmentVariables() {
        List<String> configuration = new ArrayList<>(50);
        configuration.add("\n**** APPLICATION CONFIG ****");
        configuration.add("*** CURRENT PROFILES ***");
        configuration.add(String.join(", ", environment.getActiveProfiles()));

        configuration.add("*** APPLICATION PROPERTIES ***");
        for (EnumerablePropertySource<?> p : getGroupedPropertySources()) {
            configuration.add(p.getName().trim());

            List<String> properties = Arrays.stream(p.getPropertyNames())
                    .sorted()
                    .collect(Collectors.toList());
            for (String propertyName : properties) {
                String property;
                try {
                    property = environment.getProperty(propertyName);
                } catch (Exception ex) {
                    property = "***UNDEFINED***";
                }
                configuration.add(String.format("  %s = %s", propertyName, property));
            }
        }
        log.info(String.join("\n", configuration));
    }

    /**
     * Группирует параметры по источнику.
     *
     * @return {@link List}<{@link EnumerablePropertySource}>
     */
    private List<EnumerablePropertySource<?>> getGroupedPropertySources() {
        List<EnumerablePropertySource<?>> propertiesPropertySources = new LinkedList<>();
        for (PropertySource<?> propertySource : environment.getPropertySources()) {
            if (propertySource instanceof EnumerablePropertySource) {
                propertiesPropertySources.add((EnumerablePropertySource<?>) propertySource);
            }
        }
        return propertiesPropertySources;
    }
}
