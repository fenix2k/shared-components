package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.config.properties;

import lombok.Data;

/**
 * Параметры конфигурации.
 */
@Data
public class CryptoCoreProperties {

    /** Отключить печать временной зоны. */
    private boolean disableTimeZonePrint;

    /** Отключить печать конфигурации. */
    private boolean disableEnvVarPrint;

    /** Отключить печать бинов. */
    private boolean disableBeanPrint;
}
