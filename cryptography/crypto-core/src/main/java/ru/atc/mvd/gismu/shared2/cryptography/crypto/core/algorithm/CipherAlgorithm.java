package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm;

import lombok.Getter;
import lombok.ToString;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.api.CryptoAlgorithm;

/**
 * Алгоритмы симметричного шифрования.
 */
@Getter
@ToString
@SuppressWarnings("unused")
public abstract class CipherAlgorithm extends CryptoAlgorithm {

    private final Integer keyLength;

    public CipherAlgorithm(String name, String algorithm, String identifier,
                           String oid, Integer keyLength,
                           String cryptoProvider) {
        super(name, algorithm, identifier, oid, cryptoProvider);
        this.keyLength = keyLength;
    }

    public CipherAlgorithm(String algorithm, String identifier, String oid, Integer keyLength, String cryptoProvider) {
        this(algorithm, algorithm, identifier, oid, keyLength, cryptoProvider);
    }

    public CipherAlgorithm(String algorithm, String identifier, String oid, Integer keyLength) {
        this(algorithm, algorithm, identifier, oid, keyLength, DEFAULT_CRYPTO_PROVIDER);
    }

    /**
     * Возвращает CipherAlgorithm по названию алгоритма или oid.
     *
     * @param algorithm algorithm
     * @return {@link CipherAlgorithm}
     */
    public static CipherAlgorithm fromString(String algorithm) {
        CryptoAlgorithm alg = fromIdentifier(algorithm);
        if (alg == null) {
            alg = fromOid(algorithm);
        }
        return cast(alg, CipherAlgorithm.class);
    }
}
