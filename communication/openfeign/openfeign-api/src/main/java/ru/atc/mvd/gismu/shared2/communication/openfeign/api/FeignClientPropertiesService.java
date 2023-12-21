package ru.atc.mvd.gismu.shared2.communication.openfeign.api;

import ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props.FeignClientServiceProperties;

/**
 * Интерфейс для работы с параметрами клиента.
 */
public interface FeignClientPropertiesService {

    /**
     * Получение сводной конфигурации и объединение с локальным конфигом.
     *
     * @param serviceCode код сервиса {@link String}
     * @param clientServiceProperties локальная конфигурация {@link FeignClientServiceProperties}
     * @return сводная конфигурация {@link FeignClientServiceProperties}
     */
    FeignClientServiceProperties getAndMergeServiceProperties(String serviceCode,
                                                              FeignClientServiceProperties clientServiceProperties);
}
