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
public class SymmetricCipherAlgorithm extends CipherAlgorithm {

    //CHECKSTYLE:OFF
    public static final SymmetricCipherAlgorithm blowfish =
            new SymmetricCipherAlgorithm("Blowfish", "Blowfish", "", null);
    public static final SymmetricCipherAlgorithm aes =
            new SymmetricCipherAlgorithm("AES", "AES", "", null);
    public static final SymmetricCipherAlgorithm aes_gcm =
            new SymmetricCipherAlgorithm("AES", "AES/GCM/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes_kw =
            new SymmetricCipherAlgorithm("AES", "AES/KW/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes_kwp =
            new SymmetricCipherAlgorithm("AES", "AES/KWP/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes128_gcm =
            new SymmetricCipherAlgorithm("AES", "AES_128/GCM/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes128_kw =
            new SymmetricCipherAlgorithm("AES", "AES_128/KW/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes128_kwp =
            new SymmetricCipherAlgorithm("AES", "AES_128/KWP/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes192_gcm =
            new SymmetricCipherAlgorithm("AES", "AES_192/GCM/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes192_kw =
            new SymmetricCipherAlgorithm("AES", "AES_192/KW/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes192_kwp =
            new SymmetricCipherAlgorithm("AES", "AES_192/KWP/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes256_gcm =
            new SymmetricCipherAlgorithm("AES", "AES_256/GCM/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes256_kw =
            new SymmetricCipherAlgorithm("AES", "AES_256/KW/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm aes256_kwp =
            new SymmetricCipherAlgorithm("AES", "AES_256/KWP/NoPadding", "", null);
    public static final SymmetricCipherAlgorithm pbe_hmacSha1_aes128 =
            new SymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA1AndAES_128", "", null);
    public static final SymmetricCipherAlgorithm pbe_hmacSha1_aes256 =
            new SymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA1AndAES_256", "", null);
    public static final SymmetricCipherAlgorithm pbe_hmacSha256_aes128 =
            new SymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA256AndAES_128", "", null);
    public static final SymmetricCipherAlgorithm pbe_hmacSha256_aes256 =
            new SymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA256AndAES_256", "", null);
    public static final SymmetricCipherAlgorithm pbe_hmacSha512_aes128 =
            new SymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA512AndAES_128", "", null);
    public static final SymmetricCipherAlgorithm pbe_hmacSha512_aes256 =
            new SymmetricCipherAlgorithm("PBE", "PBEWithHmacSHA512AndAES_256", "", null);
    public static final SymmetricCipherAlgorithm pbe_md5_des =
            new SymmetricCipherAlgorithm("HmacMD5", "PBEWithMD5AndDES", "", null);
    public static final SymmetricCipherAlgorithm pbe_md5_3des =
            new SymmetricCipherAlgorithm("HmacMD5", "PBEWithMD5AndTripleDES", "", null);

    public static final SymmetricCipherAlgorithm gost3412_2015 =
            new SymmetricCipherAlgorithm("GOST3412-2015", "GOST3412-2015", "", 32, "BC");
    //CHECKSTYLE:ON

    static {
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

    public SymmetricCipherAlgorithm(String name, String algorithm, String identifier,
                                    String oid, Integer keyLength,
                                    String cryptoProvider) {
        super(name, algorithm, identifier, oid, keyLength, cryptoProvider);
    }

    public SymmetricCipherAlgorithm(String algorithm, String identifier, String oid, Integer keyLength, String cryptoProvider) {
        this(algorithm, algorithm, identifier, oid, keyLength, cryptoProvider);
    }

    public SymmetricCipherAlgorithm(String algorithm, String identifier, String oid, Integer keyLength) {
        this(algorithm, algorithm, identifier, oid, keyLength, DEFAULT_CRYPTO_PROVIDER);
    }

    /**
     * Возвращает CipherAlgorithm по названию алгоритма или oid.
     *
     * @param algorithm algorithm
     * @return {@link SymmetricCipherAlgorithm}
     */
    public static SymmetricCipherAlgorithm fromString(String algorithm) {
        CryptoAlgorithm alg = fromIdentifier(algorithm);
        if (alg == null) {
            alg = fromOid(algorithm);
        }
        return cast(alg, SymmetricCipherAlgorithm.class);
    }
}
