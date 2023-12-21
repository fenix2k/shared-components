package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Абстрактный класс криптографических алгоритмов.
 */
@Getter
@RequiredArgsConstructor
@ToString
public abstract class CryptoAlgorithm {

    protected static final String DEFAULT_CRYPTO_PROVIDER = null;

    private static final Map<String, CryptoAlgorithm> ALGORITHM_MAP = new HashMap<>();

    private final String name;
    private final String algorithm;
    private final String identifier;
    private final String oid;
    private final String cryptoProvider;

    protected static Map<String, CryptoAlgorithm> getAlgorithmMap() {
        return ALGORITHM_MAP;
    }

    /**
     * Помещает CryptoAlgorithm в ALGORITHM_MAP.
     *
     * @param algorithm algorithm {@link CryptoAlgorithm}
     */
    protected static void putAlgorithm(CryptoAlgorithm algorithm) {
        ALGORITHM_MAP.put(algorithm.getIdentifier(), algorithm);
    }

    /**
     * Возвращает CryptoAlgorithm по oid алгоритма.
     *
     * @param oid oid
     * @return {@link CryptoAlgorithm}
     */
    public static CryptoAlgorithm fromOid(String oid) {
        return getAlgorithmMap().values().stream()
                .filter(alg -> alg.getOid().equals(oid.trim()))
                .findFirst().orElse(null);
    }

    /**
     * Возвращает CryptoAlgorithm по названию алгоритма.
     *
     * @param identifier identifier
     * @return {@link CryptoAlgorithm}
     */
    public static CryptoAlgorithm fromIdentifier(String identifier) {
        return getAlgorithmMap().getOrDefault(identifier, null);
    }

    /**
     * Приводит CryptoAlgorithm к type.
     *
     * @param algorithm algorithm {@link CryptoAlgorithm}
     * @param type      type {@link Class}
     * @param <T>       Class T
     * @return T extends CryptoAlgorithm
     */
    @SuppressWarnings("unchecked")
    protected static <T extends CryptoAlgorithm> T cast(CryptoAlgorithm algorithm, Class<T> type) {
        if (algorithm == null) {
            return null;
        }
        if (type.isInstance(algorithm)) {
            return (T) algorithm;
        } else {
            throw new ClassCastException("Cannot cast algorithm to class " + type);
        }
    }
}
