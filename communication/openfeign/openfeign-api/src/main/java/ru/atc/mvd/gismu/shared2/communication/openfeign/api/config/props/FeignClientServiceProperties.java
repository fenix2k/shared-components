package ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props;

import feign.Logger;
import lombok.Data;

/**
 * Параметры сервиса.
 */
@Data
public class FeignClientServiceProperties {

    /** Наименование сервиса. */
    private String serviceName;
    /** URL адрес сервиса. */
    private String serviceUrl;
    /** Общие параметры аутентификации. */
    private FeignClientAuthOptions auth = new FeignClientAuthOptions();
    /** Параметры запроса. */
    private FeignClientRequestOptions requestOptions = new FeignClientRequestOptions();
    /** Параметра мониторинга. */
    private FeignClientMicrometerOptions micrometerOptions = new FeignClientMicrometerOptions();
    /** Уровень логирования. */
    private Logger.Level logLevel = Logger.Level.NONE;
}
