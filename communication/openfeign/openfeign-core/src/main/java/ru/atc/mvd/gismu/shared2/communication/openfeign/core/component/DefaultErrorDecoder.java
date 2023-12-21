package ru.atc.mvd.gismu.shared2.communication.openfeign.core.component;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Обработчик ошибки по умолчанию.
 */
@Slf4j
public class DefaultErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        log.debug("HTTP request [{} {}] error with response. Params: methodKey=[{}], responseBody=[{}]",
                response.request().httpMethod(), response.request().url(), methodKey, response);
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
