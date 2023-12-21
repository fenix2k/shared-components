package ru.atc.mvd.gismu.shared2.communication.openfeign.core.config.properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props.FeignClientProperties;

/**
 * Параметры feign клиентов.
 */
@Component
@ConfigurationProperties(prefix = "openfeign")
@ConditionalOnProperty(value = "openfeign.enabled", matchIfMissing = true)
public class SpringFeignClientProperties extends FeignClientProperties {
}
