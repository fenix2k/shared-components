package ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props;

import lombok.Data;

import java.util.Map;

/**
 * Параметры feign.
 */
@Data
public class FeignClientProperties {

    /** Идентификатор клиента. */
    private String identity;
    /** Параметры запроса по-умолчанию. */
    private FeignClientDefaultProperties defaults = new FeignClientDefaultProperties();
    /** Сервисы. */
    private Map<String, FeignClientServiceProperties> services;
}
