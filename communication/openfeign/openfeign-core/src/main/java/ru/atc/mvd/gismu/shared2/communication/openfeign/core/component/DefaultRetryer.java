package ru.atc.mvd.gismu.shared2.communication.openfeign.core.component;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

/**
 * Retryer по-умолчанию.
 */
@Slf4j
public class DefaultRetryer implements Retryer {

    private final Retryer defaultRetryer;

    public DefaultRetryer(long period, long maxPeriod, int maxAttempts) {
        defaultRetryer = new Retryer.Default(period, maxPeriod, maxAttempts);
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        log.debug("RetryException: {}", e.getMessage());
        defaultRetryer.continueOrPropagate(e);
    }

    @Override
    public Retryer clone() {
        try {
            // spotbugs fix
            super.clone();
        } catch (Exception ex) {
            log.error("Retryer clone exception: {}", ex.getLocalizedMessage(), ex);
        }
        return defaultRetryer.clone();
    }
}
