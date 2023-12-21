package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm;

import lombok.Getter;
import lombok.ToString;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.api.CryptoAlgorithm;

/**
 * Алгоритмы Hmac.
 */
@Getter
@ToString
@SuppressWarnings("unused")
public class HmacAlgorithm extends CryptoAlgorithm {

    //CHECKSTYLE:OFF
    public final static HmacAlgorithm hmacMd5 = new HmacAlgorithm("MD5", "HmacMD5", null);
    public final static HmacAlgorithm hmacSha1 = new HmacAlgorithm("SHA", "HmacSHA1", null);
    public final static HmacAlgorithm hmacSha224 = new HmacAlgorithm("SHA", "HmacSHA224", null);
    public final static HmacAlgorithm hmacSha256 = new HmacAlgorithm("SHA", "HmacSHA256", null);
    public final static HmacAlgorithm hmacSha384 = new HmacAlgorithm("MD5", "HmacSHA384", null);
    public final static HmacAlgorithm hmacSha512 = new HmacAlgorithm("SHA", "HmacSHA512", null);
    public final static HmacAlgorithm hmacSha512_224 = new HmacAlgorithm("SHA", "HmacSHA512/224", null);
    public final static HmacAlgorithm hmacSha512_256 = new HmacAlgorithm("SHA", "HmacSHA512/256", null);

    public final static HmacAlgorithm hmacSha3_224 = new HmacAlgorithm("SHA", "HmacSHA3-224", null);
    public final static HmacAlgorithm hmacSha3_256 = new HmacAlgorithm("SHA", "HmacSHA3-256", null);
    public final static HmacAlgorithm hmacSha3_384 = new HmacAlgorithm("SHA", "HmacSHA3-384", null);
    public final static HmacAlgorithm hmacSha3_512 = new HmacAlgorithm("SHA", "HmacSHA3-512", null);
    //CHECKSTYLE:ON

    static {
        putAlgorithm(hmacMd5);
        putAlgorithm(hmacSha1);
        putAlgorithm(hmacSha224);
        putAlgorithm(hmacSha256);
        putAlgorithm(hmacSha384);
        putAlgorithm(hmacSha512);
        putAlgorithm(hmacSha512_224);
        putAlgorithm(hmacSha512_256);
        putAlgorithm(hmacSha3_224);
        putAlgorithm(hmacSha3_256);
        putAlgorithm(hmacSha3_384);
        putAlgorithm(hmacSha3_512);
    }

    public HmacAlgorithm(String name, String algorithm, String identifier, String oid, String cryptoProvider) {
        super(name, algorithm, identifier, oid, cryptoProvider);
    }

    public HmacAlgorithm(String algorithm, String identifier, String oid, String cryptoProvider) {
        super(algorithm, algorithm, identifier, oid, cryptoProvider);
    }

    public HmacAlgorithm(String algorithm, String identifier, String oid) {
        super(algorithm, algorithm, identifier, oid, DEFAULT_CRYPTO_PROVIDER);
    }

    /**
     * Возвращает HmacAlgorithm по названию алгоритма или oid.
     *
     * @param algorithm algorithm
     * @return {@link HmacAlgorithm}
     */
    public static HmacAlgorithm fromString(String algorithm) {
        CryptoAlgorithm alg = fromIdentifier(algorithm);
        if (alg == null) {
            alg = fromOid(algorithm);
        }
        return cast(alg, HmacAlgorithm.class);
    }
}
