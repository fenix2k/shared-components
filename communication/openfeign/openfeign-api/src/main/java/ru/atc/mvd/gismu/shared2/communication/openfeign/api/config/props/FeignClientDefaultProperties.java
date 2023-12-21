package ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props;

import feign.Logger;
import lombok.Data;

/**
 * Параметры feign по-умолчанию.
 */
@Data
public class FeignClientDefaultProperties {

    /** Общие параметры аутентификации. */
    private FeignClientAuthOptions auth = new FeignClientAuthOptions();
    /** Параметры запроса. */
    private FeignClientRequestOptions requestOptions = new FeignClientRequestOptions();
    /** Параметра мониторинга. */
    private FeignClientMicrometerOptions micrometerOptions = new FeignClientMicrometerOptions();
    /** Уровень логирования. */
    private Logger.Level logLevel = Logger.Level.NONE;
}
