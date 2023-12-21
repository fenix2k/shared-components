package ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props;

import lombok.Data;

/**
 * Параметра мониторинга.
 */
@Data
public class FeignClientMicrometerOptions {

    /** Признак включения. */
    private boolean enabled;
}
