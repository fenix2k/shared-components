package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm;

import lombok.Getter;
import lombok.ToString;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.api.CryptoAlgorithm;

/**
 * Алгоритмы подписи.
 */
@Getter
@ToString
@SuppressWarnings("unused")
public class SignatureAlgorithm extends CryptoAlgorithm {

    //CHECKSTYLE:OFF
    public static final SignatureAlgorithm gost2012_withhash_256 = new SignatureAlgorithm(
            "GOST3411-2012-256WITHECGOST3410-2012-256",
            "ECGOST3410-2012",
            "GOST3411-2012-256WITHECGOST3410-2012-256",
            "1.2.643.7.1.1.3.2",
            "Tc26-Gost-3410-12-256-paramSetA",
            "X.509",
            null,
            "BC");
    public static final SignatureAlgorithm gost2012_withhash_512 = new SignatureAlgorithm(
            "GOST3411-2012-512WITHECGOST3410-2012-512",
            "ECGOST3410-2012",
            "GOST3411-2012-512WITHECGOST3410-2012-512",
            "1.2.643.7.1.1.3.3",
            "Tc26-Gost-3410-12-512-paramSetA",
            "X.509",
            null,
            "BC");
    public static final SignatureAlgorithm gost2012_256 = new SignatureAlgorithm(
            "ECGOST3410-2012-256",
            "ECGOST3410-2012",
            "ECGOST3410-2012-256",
            "1.2.643.7.1.1.1.1",
            "Tc26-Gost-3410-12-256-paramSetA",
            "X.509",
            null,
            "BC");
    public static final SignatureAlgorithm gost2012_512 = new SignatureAlgorithm(
            "ECGOST3410-2012-512",
            "ECGOST3410-2012",
            "ECGOST3410-2012-512",
            "1.2.643.7.1.1.1.2",
            "Tc26-Gost-3410-12-512-paramSetA",
            "X.509",
            null,
            "BC");
    public final static SignatureAlgorithm gost2001 = new SignatureAlgorithm(
            "GOST3411withECGOST3410",
            "ECGOST3410",
            "",
            "GOST3411withECGOST3410",
            "GostR3410-2001-CryptoPro-A",
            "X.509",
            null,
            "BC"
    );
    public final static SignatureAlgorithm ecdsa256 = new SignatureAlgorithm(
            "SHA256withECDSA",
            "ECDSA",
            "SHA256withECDSA",
            "",
            "P-256",
            "X.509",
            null,
            DEFAULT_CRYPTO_PROVIDER
    );
    public final static SignatureAlgorithm ecdsa384 = new SignatureAlgorithm(
            "SHA384withECDSA",
            "ECDSA",
            "SHA384withECDSA",
            "",
            "P-384",
            "X.509",
            null,
            DEFAULT_CRYPTO_PROVIDER
    );
    public final static SignatureAlgorithm ecdsa512 = new SignatureAlgorithm(
            "SHA512withECDSA",
            "ECDSA",
            "SHA512withECDSA",
            "",
            "P-521",
            "X.509",
            null,
            DEFAULT_CRYPTO_PROVIDER
    );
    public final static SignatureAlgorithm rsaSha256_2048 = new SignatureAlgorithm(
            "SHA256WithRSAEncryption",
            "RSA",
            "SHA256WithRSAEncryption",
            "",
            "",
            "X.509",
            2048,
            DEFAULT_CRYPTO_PROVIDER
    );
    public final static SignatureAlgorithm rsaSha256_4096 = new SignatureAlgorithm(
            "SHA256WithRSAEncryption",
            "RSA",
            "",
            "SHA256WithRSAEncryption",
            "",
            "X.509",
            4096,
            DEFAULT_CRYPTO_PROVIDER
    );
    public final static SignatureAlgorithm rsaSha1_2048 = new SignatureAlgorithm(
            "SHA1WithRSAEncryption",
            "RSA",
            "SHA1WithRSAEncryption",
            "",
            "rsaSha1_2048",
            "X.509",
            2048,
            DEFAULT_CRYPTO_PROVIDER
    );
    //CHECKSTYLE:ON

    static {
        putAlgorithm(gost2012_withhash_256);
        putAlgorithm(gost2012_withhash_512);
        putAlgorithm(gost2012_256);
        putAlgorithm(gost2012_512);
        putAlgorithm(gost2001);
        putAlgorithm(ecdsa256);
        putAlgorithm(ecdsa384);
        putAlgorithm(ecdsa512);
        putAlgorithm(rsaSha256_2048);
        putAlgorithm(rsaSha256_4096);
        putAlgorithm(rsaSha1_2048);
    }

    private final String ellipticCurve;
    private final String certificateType;
    private final Integer keyLength;

    public SignatureAlgorithm(String name, String algorithm, String identifier, String oid, String ellipticCurve,
                              String certificateType, Integer keyLength, String cryptoProvider) {
        super(name, algorithm, identifier, oid, cryptoProvider);
        this.ellipticCurve = ellipticCurve;
        this.certificateType = certificateType;
        this.keyLength = keyLength;
    }

    /**
     * Возвращает SignatureAlgorithm по названию алгоритма или oid.
     *
     * @param algorithm algorithm
     * @return {@link SignatureAlgorithm}
     */
    public static SignatureAlgorithm fromString(String algorithm) {
        CryptoAlgorithm alg = fromIdentifier(algorithm);
        if (alg == null) {
            alg = fromOid(algorithm);
        }
        return cast(alg, SignatureAlgorithm.class);
    }
}
