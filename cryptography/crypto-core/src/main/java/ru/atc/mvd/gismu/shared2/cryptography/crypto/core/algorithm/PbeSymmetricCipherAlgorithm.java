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
public class PbeSymmetricCipherAlgorithm extends SymmetricCipherAlgorithm {

    //CHECKSTYLE:OFF
    public static final PbeSymmetricCipherAlgorithm pbe_hmacSha1_aes128 =
            new PbeSymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA1AndAES_128", "", null);
    public static final PbeSymmetricCipherAlgorithm pbe_hmacSha1_aes256 =
            new PbeSymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA1AndAES_256", "", null);
    public static final PbeSymmetricCipherAlgorithm pbe_hmacSha256_aes128 =
            new PbeSymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA256AndAES_128", "", null);
    public static final PbeSymmetricCipherAlgorithm pbe_hmacSha256_aes256 =
            new PbeSymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA256AndAES_256", "", null);
    public static final PbeSymmetricCipherAlgorithm pbe_hmacSha512_aes128 =
            new PbeSymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA512AndAES_128", "", null);
    public static final PbeSymmetricCipherAlgorithm pbe_hmacSha512_aes256 =
            new PbeSymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA512AndAES_256", "", null);
    public static final PbeSymmetricCipherAlgorithm pbe_md5_des =
            new PbeSymmetricCipherAlgorithm("HmacMD5", "PBEWithMD5AndDES", "", null);
    public static final PbeSymmetricCipherAlgorithm pbe_md5_3des =
            new PbeSymmetricCipherAlgorithm("HmacMD5", "PBEWithMD5AndTripleDES", "", null);
    //CHECKSTYLE:ON

    static {
        putAlgorithm(pbe_hmacSha1_aes128);
        putAlgorithm(pbe_hmacSha1_aes256);
        putAlgorithm(pbe_hmacSha256_aes128);
        putAlgorithm(pbe_hmacSha256_aes256);
        putAlgorithm(pbe_hmacSha512_aes128);
        putAlgorithm(pbe_hmacSha512_aes256);
        putAlgorithm(pbe_md5_des);
        putAlgorithm(pbe_md5_3des);
    }

    public PbeSymmetricCipherAlgorithm(String name, String algorithm, String identifier,
                                       String oid, Integer keyLength,
                                       String cryptoProvider) {
        super(name, algorithm, identifier, oid, keyLength, cryptoProvider);
    }

    public PbeSymmetricCipherAlgorithm(String algorithm, String identifier, String oid, Integer keyLength, String cryptoProvider) {
        this(algorithm, algorithm, identifier, oid, keyLength, cryptoProvider);
    }

    public PbeSymmetricCipherAlgorithm(String algorithm, String identifier, String oid, Integer keyLength) {
        this(algorithm, algorithm, identifier, oid, keyLength, DEFAULT_CRYPTO_PROVIDER);
    }

    /**
     * Возвращает CipherAlgorithm по названию алгоритма или oid.
     *
     * @param algorithm algorithm
     * @return {@link PbeSymmetricCipherAlgorithm}
     */
    public static PbeSymmetricCipherAlgorithm fromString(String algorithm) {
        CryptoAlgorithm alg = fromIdentifier(algorithm);
        if (alg == null) {
            alg = fromOid(algorithm);
        }
        return cast(alg, PbeSymmetricCipherAlgorithm.class);
    }
}
