package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Типы записи в хранилище.
 */
@RequiredArgsConstructor
@Getter
public enum CryptoStoreEntryTypes {

    PKEY("PrivateKey"),
    SKEY("SecretKey"),
    CERT("TrustedCertificate");

    private final String description;
}
