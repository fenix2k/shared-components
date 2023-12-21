package ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props;

import lombok.Data;

/**
 * Параметры повторных запросов.
 */
@Data
public class FeignClientRetryOptions {

    /** Повторять ли запрос в случае неудачи. */
    private boolean allowRetry = true;
    /** Минимальный интервал повтора. */
    private long period = 100;
    /** Максимальный интервал повтора. */
    private long maxPeriod = 1_000;
    /** Максимальное кол-во повторов. */
    private int maxAttempts = 5;
}
