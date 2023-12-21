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
public class AsymmetricCipherAlgorithm extends CipherAlgorithm {

    //CHECKSTYLE:OFF
    public static final AsymmetricCipherAlgorithm rsa =
            new AsymmetricCipherAlgorithm("RSA", "RSA", "", null);
    public static final AsymmetricCipherAlgorithm blowfish =
            new AsymmetricCipherAlgorithm("Blowfish", "Blowfish", "", null);
    public static final AsymmetricCipherAlgorithm aes =
            new AsymmetricCipherAlgorithm("AES", "AES", "", null);
    public static final AsymmetricCipherAlgorithm aes_gcm =
            new AsymmetricCipherAlgorithm("AES", "AES/GCM/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes_kw =
            new AsymmetricCipherAlgorithm("AES", "AES/KW/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes_kwp =
            new AsymmetricCipherAlgorithm("AES", "AES/KWP/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes128_gcm =
            new AsymmetricCipherAlgorithm("AES", "AES_128/GCM/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes128_kw =
            new AsymmetricCipherAlgorithm("AES", "AES_128/KW/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes128_kwp =
            new AsymmetricCipherAlgorithm("AES", "AES_128/KWP/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes192_gcm =
            new AsymmetricCipherAlgorithm("AES", "AES_192/GCM/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes192_kw =
            new AsymmetricCipherAlgorithm("AES", "AES_192/KW/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes192_kwp =
            new AsymmetricCipherAlgorithm("AES", "AES_192/KWP/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes256_gcm =
            new AsymmetricCipherAlgorithm("AES", "AES_256/GCM/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes256_kw =
            new AsymmetricCipherAlgorithm("AES", "AES_256/KW/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm aes256_kwp =
            new AsymmetricCipherAlgorithm("AES", "AES_256/KWP/NoPadding", "", null);
    public static final AsymmetricCipherAlgorithm pbe_hmacSha1_aes128 =
            new AsymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA1AndAES_128", "", null);
    public static final AsymmetricCipherAlgorithm pbe_hmacSha1_aes256 =
            new AsymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA1AndAES_256", "", null);
    public static final AsymmetricCipherAlgorithm pbe_hmacSha256_aes128 =
            new AsymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA256AndAES_128", "", null);
    public static final AsymmetricCipherAlgorithm pbe_hmacSha256_aes256 =
            new AsymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA256AndAES_256", "", null);
    public static final AsymmetricCipherAlgorithm pbe_hmacSha512_aes128 =
            new AsymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA512AndAES_128", "", null);
    public static final AsymmetricCipherAlgorithm pbe_hmacSha512_aes256 =
            new AsymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA512AndAES_256", "", null);
    public static final AsymmetricCipherAlgorithm pbe_md5_des =
            new AsymmetricCipherAlgorithm("DES", "PBEWithMD5AndDES", "", null);
    public static final AsymmetricCipherAlgorithm pbe_md5_3des =
            new AsymmetricCipherAlgorithm("DES", "PBEWithMD5AndTripleDES", "", null);

    public static final AsymmetricCipherAlgorithm gost3412_2015 =
            new AsymmetricCipherAlgorithm("GOST3412-2015", "GOST3412-2015", "", 32, "BC");
    //CHECKSTYLE:ON

    static {
        putAlgorithm(rsa);
        putAlgorithm(blowfish);
        putAlgorithm(aes);
        putAlgorithm(aes_gcm);
        putAlgorithm(aes_kw);
        putAlgorithm(aes_kwp);
        putAlgorithm(aes128_gcm);
        putAlgorithm(aes128_kw);
        putAlgorithm(aes128_kwp);
        putAlgorithm(aes192_gcm);
        putAlgorithm(aes192_kw);
        putAlgorithm(aes192_kwp);
        putAlgorithm(aes256_gcm);
        putAlgorithm(aes256_kw);
        putAlgorithm(aes256_kwp);
        putAlgorithm(pbe_hmacSha1_aes128);
        putAlgorithm(pbe_hmacSha1_aes256);
        putAlgorithm(pbe_hmacSha256_aes128);
        putAlgorithm(pbe_hmacSha256_aes256);
        putAlgorithm(pbe_hmacSha512_aes128);
        putAlgorithm(pbe_hmacSha512_aes256);
        putAlgorithm(pbe_md5_des);
        putAlgorithm(pbe_md5_3des);

        // BC Provider
        putAlgorithm(gost3412_2015);
    }

    public AsymmetricCipherAlgorithm(String name, String algorithm, String identifier,
                                     String oid, Integer keyLength,
                                     String cryptoProvider) {
        super(name, algorithm, identifier, oid, keyLength, cryptoProvider);
    }

    public AsymmetricCipherAlgorithm(String algorithm, String identifier, String oid, Integer keyLength, String cryptoProvider) {
        this(algorithm, algorithm, identifier, oid, keyLength, cryptoProvider);
    }

    public AsymmetricCipherAlgorithm(String algorithm, String identifier, String oid, Integer keyLength) {
        this(algorithm, algorithm, identifier, oid, keyLength, DEFAULT_CRYPTO_PROVIDER);
    }

    /**
     * Возвращает CipherAlgorithm по названию алгоритма или oid.
     *
     * @param algorithm algorithm
     * @return {@link AsymmetricCipherAlgorithm}
     */
    public static AsymmetricCipherAlgorithm fromString(String algorithm) {
        CryptoAlgorithm alg = fromIdentifier(algorithm);
        if (alg == null) {
            alg = fromOid(algorithm);
        }
        return cast(alg, AsymmetricCipherAlgorithm.class);
    }
}
