package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm;

import lombok.Getter;
import lombok.ToString;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.api.CryptoAlgorithm;

/**
 * Алгоритмы хеширования.
 */
@Getter
@ToString
@SuppressWarnings("unused")
public class HashAlgorithm extends CryptoAlgorithm {

    //CHECKSTYLE:OFF
    public final static HashAlgorithm md2 = new HashAlgorithm("MD", "MD2", null);
    public final static HashAlgorithm md5 = new HashAlgorithm("MD", "MD5", null);
    public final static HashAlgorithm sha1 = new HashAlgorithm("SHA", "SHA1", null);
    public final static HashAlgorithm sha224 = new HashAlgorithm("SHA", "SHA224", null);
    public final static HashAlgorithm sha256 = new HashAlgorithm("SHA", "SHA256", null);
    public final static HashAlgorithm sha384 = new HashAlgorithm("SHA", "SHA384", null);
    public final static HashAlgorithm sha512 = new HashAlgorithm("SHA", "SHA512", null);
    public final static HashAlgorithm sha3_224 = new HashAlgorithm("SHA3", "SHA3-224", null);
    public final static HashAlgorithm sha3_256 = new HashAlgorithm("SHA3", "SHA3-256", null);
    public final static HashAlgorithm sha3_384 = new HashAlgorithm("SHA3", "SHA3-384", null);
    public final static HashAlgorithm sha3_512 = new HashAlgorithm("SHA3", "SHA3-512", null);

    // BC Provider
    public final static HashAlgorithm md4 =
            new HashAlgorithm("MD", "MD4", "BC");
    public final static HashAlgorithm shake128 =
            new HashAlgorithm("SHAKE", "SHAKE128", "", "BC");
    public final static HashAlgorithm shake256 =
            new HashAlgorithm("SHAKE", "SHAKE256", "", "BC");
    public final static HashAlgorithm ripemd128 =
            new HashAlgorithm("RIPEMD", "RIPEMD128", "", "BC");
    public final static HashAlgorithm ripemd160 =
            new HashAlgorithm("RIPEMD", "RIPEMD160", "", "BC");
    public final static HashAlgorithm ripemd256 =
            new HashAlgorithm("RIPEMD", "RIPEMD256", "", "BC");
    public final static HashAlgorithm sm3 =
            new HashAlgorithm("SM", "SM3", "", "BC");
    public static final HashAlgorithm gost2012_256 =
            new HashAlgorithm("GOST3411-2012", "GOST3411-2012-256", "1.2.643.7.1.1.2.2", "BC");
    public static final HashAlgorithm gost2012_512 =
            new HashAlgorithm("GOST3411-2012", "GOST3411-2012-512", "1.2.643.7.1.1.2.3", "BC");
    public final static HashAlgorithm gost94 =
            new HashAlgorithm("GOST3411-94", "GOST3411", "", "BC");
    //CHECKSTYLE:ON

    static {
        putAlgorithm(md2);
        putAlgorithm(md5);
        putAlgorithm(sha1);
        putAlgorithm(sha224);
        putAlgorithm(sha256);
        putAlgorithm(sha384);
        putAlgorithm(sha512);
        putAlgorithm(sha3_224);
        putAlgorithm(sha3_256);
        putAlgorithm(sha3_384);
        putAlgorithm(sha3_512);

        // BC Provider
        putAlgorithm(md4);
        putAlgorithm(shake128);
        putAlgorithm(shake256);
        putAlgorithm(ripemd128);
        putAlgorithm(ripemd160);
        putAlgorithm(ripemd256);
        putAlgorithm(sm3);
        putAlgorithm(gost2012_256);
        putAlgorithm(gost2012_512);
        putAlgorithm(gost94);
    }

    public HashAlgorithm(String name, String algorithm, String identifier, String oid, String cryptoProvider) {
        super(name, algorithm, identifier, oid, cryptoProvider);
    }

    public HashAlgorithm(String algorithm, String identifier, String oid, String cryptoProvider) {
        this(algorithm, algorithm, identifier, oid, cryptoProvider);
    }

    public HashAlgorithm(String algorithm, String identifier, String oid) {
        this(algorithm, algorithm, identifier, oid, DEFAULT_CRYPTO_PROVIDER);
    }

    /**
     * Возвращает HashAlgorithm по названию алгоритма или oid.
     *
     * @param algorithm algorithm
     * @return {@link HashAlgorithm}
     */
    public static HashAlgorithm fromString(String algorithm) {
        CryptoAlgorithm alg = fromIdentifier(algorithm);
        if (alg == null) {
            alg = fromOid(algorithm);
        }
        return cast(alg, HashAlgorithm.class);
    }
}
