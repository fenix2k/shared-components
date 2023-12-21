package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.keystore.configuration;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

/**
 * Конфигурация KeyStoreProvider.
 */
@Getter
@Setter
public class AbstractKeyStoreConfigProperties {

    /**  Путь к keystore. */
    private Path path;

    /** Пароль от keystore. */
    private String password;

    /** Тип keystore. */
    private String type = "BKS";

    /** Автосоздание keystore. */
    private Boolean autocreate = true;
}
