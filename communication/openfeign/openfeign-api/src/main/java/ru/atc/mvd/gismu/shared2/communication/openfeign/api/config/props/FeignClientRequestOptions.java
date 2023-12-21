package ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props;

import lombok.Data;

/**
 * Параметры запроса.
 */
@Data
public class FeignClientRequestOptions {

    /** Таймаут соединения. */
    private long connectTimeout = 10_000;
    /** Таймаут чтения данных при запросе. */
    private long readTimeOut = 60_000;
    /** Следовать редиректам. */
    private boolean followRedirects = true;
    /** Параметры повторных запросов. */
    private FeignClientRetryOptions retryOptions = new FeignClientRetryOptions();
}
