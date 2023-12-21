package ru.atc.mvd.gismu.shared2.communication.kafka.api.config.properties;

import lombok.Data;

/**
 * Параметры конфигурации.
 */
@Data
public class KafkaProperties {

    /** Включить клиент. */
    private boolean enabled = true;
    /** Идентификатор клиента. */
    private String identity;
    private String bootstrapServers;
}
