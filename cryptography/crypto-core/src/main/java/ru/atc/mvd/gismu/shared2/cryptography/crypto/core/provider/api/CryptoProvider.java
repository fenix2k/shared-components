package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.provider.api;

import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.AsymmetricCipherAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.HashAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.HmacAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.PbeSymmetricCipherAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.SignatureAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.SymmetricCipherAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.exception.CryptoProviderException;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.lang.Assert;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.provider.utils.CryptoOperationErrors;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.util.Objects;

/**
 * Интерфейс криптопровайдера.
 */
@SuppressWarnings("unused")
public interface CryptoProvider {

    /**
     * Хэширует данные data указанным алгоритмом хеширования hashAlgorithm.
     *
     * @param hashAlgorithm hashAlgorithm {@link HashAlgorithm}
     * @param data          data
     * @return byte[]
     */
    default byte[] hash(HashAlgorithm hashAlgorithm, final byte[] data) throws CryptoProviderException {
        Assert.notNull(hashAlgorithm, CryptoOperationErrors.INVALID_INPUT_ALGORITHM);
        Assert.notEmpty(data, CryptoOperationErrors.INVALID_INPUT_DATA);

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(hashAlgorithm.getIdentifier());
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s", CryptoOperationErrors.HASH_ALGORITHM_NOT_FOUND, hashAlgorithm), ex);
        }
        return digest.digest(data);
    }

    /**
     * Генерирует HMAC хэш.
     *
     * @param data      data
     * @param secretKey secretKey {@link SecretKey}
     * @return byte[]
     */
    default byte[] hmac(final byte[] data, SecretKey secretKey) throws CryptoProviderException {
        Assert.notEmpty(data, CryptoOperationErrors.INVALID_INPUT_DATA);
        Assert.notNull(secretKey, CryptoOperationErrors.NO_KEY_PRESENT);

        try {
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            mac.update(data);
            return mac.doFinal();
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s", CryptoOperationErrors.HASH_ALGORITHM_NOT_FOUND, secretKey.getAlgorithm()), ex);
        } catch (InvalidKeyException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.INVALID_KEY, ex);
        }
    }

    /**
     * Шифрует данные data симметричным алгоритмом algorithm и сертификатом publicKey.
     *
     * @param algorithm algorithm {@link SymmetricCipherAlgorithm}
     * @param data      data
     * @param key       publicKey {@link SecretKey}
     * @param paramSpec paramSpec {@link AlgorithmParameterSpec}
     * @return byte[]
     * @throws CryptoProviderException Crypto Operation Exception
     */
    byte[] encrypt(SymmetricCipherAlgorithm algorithm, byte[] data, SecretKey key, AlgorithmParameterSpec paramSpec)
            throws CryptoProviderException;

    /**
     * Шифрует данные data ассиметричным алгоритмом algorithm и сертификатом publicKey.
     *
     * @param algorithm algorithm {@link AsymmetricCipherAlgorithm}
     * @param data      data
     * @param publicKey publicKey {@link PublicKey}
     * @return byte[]
     * @throws CryptoProviderException Crypto Operation Exception
     */
    byte[] encrypt(AsymmetricCipherAlgorithm algorithm, byte[] data, PublicKey publicKey)
            throws CryptoProviderException;

    /**
     * Шифрует данные data симметричным алгоритмом GOST3412_2015.
     *
     * @param data      data
     * @param secretKey secretKey {@link SecretKey}
     * @return byte[]
     */
    default byte[] encryptWithGost34122015(final byte[] data, SecretKey secretKey) throws CryptoProviderException {
        throw new CryptoProviderException("not supported");
    }

    /**
     * Расшифровывает данные encryptedData при помощи ключа privateKey.
     *
     * @param encryptedData encryptedData
     * @param key           privateKey {@link SecretKey}
     * @param paramSpec     paramSpec {@link AlgorithmParameterSpec}
     * @return byte[]
     * @throws CryptoProviderException Crypto Operation Exception
     */
    byte[] decrypt(byte[] encryptedData, SecretKey key, AlgorithmParameterSpec paramSpec) throws CryptoProviderException;

    /**
     * Расшифровывает данные encryptedData при помощи ключа privateKey.
     *
     * @param encryptedData encryptedData
     * @param privateKey    privateKey {@link PrivateKey}
     * @return byte[]
     * @throws CryptoProviderException Crypto Operation Exception
     */
    byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) throws CryptoProviderException;

    /**
     * Расшифровывает данные data симметричным алгоритмом GOST3412_2015.
     *
     * @param data      data
     * @param secretKey secretKey {@link SecretKey}
     * @return byte[]
     */
    default byte[] decryptWithGost34122015(byte[] data, SecretKey secretKey) throws CryptoProviderException {
        throw new CryptoProviderException("not supported");
    }

    /**
     * Подписывает данные data приватным ключом privateKey и сертификатом publicKey
     * используя алгоритм algorithm и возвращает подпись.
     *
     * @param data             data
     * @param privateKey       privateKey {@link PrivateKey}
     * @param publicKey        publicKey {@link X509Certificate}
     * @param certificateChain publicKey {@link X509Certificate}
     * @return byte[]
     * @throws CryptoProviderException Crypto Operation Exception
     */
    byte[] sign(byte[] data, PrivateKey privateKey, X509Certificate publicKey,
                X509Certificate[] certificateChain)
            throws CryptoProviderException;

    /**
     * Проверяет соответствие исходных данных data подписи signature.
     *
     * @param data      data
     * @param signature signature
     * @param publicKey publicKey {@link X509Certificate}
     * @return результат проверки
     * @throws CryptoProviderException Crypto Operation Exception
     */
    boolean verifySign(byte[] data, byte[] signature, X509Certificate publicKey) throws CryptoProviderException;

    /**
     * Проверяет соответствие исходных данных data подписи signature.
     *
     * @param data      data
     * @param signature signature
     * @return результат проверки
     * @throws CryptoProviderException Crypto Operation Exception
     */
    boolean verifySign(byte[] data, byte[] signature) throws CryptoProviderException;

    /**
     * Генерирует публичный и приватные ключи по заданному алгоритму.
     *
     * @param algorithm algorithm {@link AsymmetricCipherAlgorithm}
     * @return {@link KeyPair}
     * @throws CryptoProviderException Crypto Operation Exception
     */
    KeyPair generateKeyPair(AsymmetricCipherAlgorithm algorithm) throws CryptoProviderException;

    /**
     * Генерирует публичный и приватные ключи по заданному алгоритму.
     *
     * @param algorithm algorithm {@link SignatureAlgorithm}
     * @return {@link KeyPair}
     * @throws CryptoProviderException Crypto Operation Exception
     */
    KeyPair generateKeyPair(SignatureAlgorithm algorithm) throws CryptoProviderException;

    /**
     * Генерирует ключ шифрования по заданному алгоритму.
     *
     * @param algorithm algorithm {@link SymmetricCipherAlgorithm}
     * @return {@link SecretKey}
     * @throws CryptoProviderException Crypto Operation Exception
     */
    SecretKey generateSecretKey(SymmetricCipherAlgorithm algorithm) throws CryptoProviderException;

    /**
     * Генерирует ключ шифрования по заданному алгоритму.
     *
     * @param algorithm  algorithm {@link PbeSymmetricCipherAlgorithm}
     * @param password   password
     * @param paramSpec  paramSpec {@link PBEParameterSpec}
     * @return {@link SecretKey}
     * @throws CryptoProviderException Crypto Operation Exception
     */
    SecretKey generateSecretKey(PbeSymmetricCipherAlgorithm algorithm, String password, PBEParameterSpec paramSpec)
            throws CryptoProviderException;

    /**
     * Генерирует Hmac ключ по заданному алгоритму.
     *
     * @param algorithm    algorithm {@link HmacAlgorithm}
     * @param secretString secretString
     * @return {@link SecretKey}
     * @throws CryptoProviderException Crypto Operation Exception
     */
    SecretKey generateSecretKey(HmacAlgorithm algorithm, String secretString);

    /**
     * Выпускает самоподписанный сертификат.
     *
     * @param alias     alias
     * @param keyPair   keyPair {@link KeyPair}
     * @param algorithm algorithm {@link SignatureAlgorithm}
     * @return {@link X509Certificate}
     * @throws CryptoProviderException Crypto Operation Exception
     */
    X509Certificate generateSelfSignedCertificate(String alias, KeyPair keyPair, SignatureAlgorithm algorithm)
            throws CryptoProviderException;

    /**
     * Генерирует пару ключом и выпускает самоподписанный сертификат.
     *
     * @param alias     alias
     * @param algorithm algorithm
     * @return {@link X509Certificate}
     * @throws CryptoProviderException Crypto Operation Exception
     */
    X509Certificate generateSelfSignedCertificate(String alias, SignatureAlgorithm algorithm)
            throws CryptoProviderException;

    /**
     * Возвращает соответсвующий алгоритму и криптопровайдеру генератор ключей.
     *
     * @param signatureAlgorithm signatureAlgorithm {@link SignatureAlgorithm}
     * @return {@link KeyPairGenerator}
     * @throws NoSuchAlgorithmException           No Such Algorithm Exception
     * @throws NoSuchProviderException            No Such Provider Exception
     * @throws InvalidAlgorithmParameterException Invalid Algorithm Parameter Exception
     */
    static KeyPairGenerator initKeyGen(SignatureAlgorithm signatureAlgorithm) throws NoSuchAlgorithmException,
            NoSuchProviderException, InvalidAlgorithmParameterException {

        KeyPairGenerator keypairGen = signatureAlgorithm.getCryptoProvider() == null ?
                KeyPairGenerator.getInstance(signatureAlgorithm.getAlgorithm()) :
                KeyPairGenerator.getInstance(signatureAlgorithm.getAlgorithm(), signatureAlgorithm.getCryptoProvider());
        if (signatureAlgorithm.getEllipticCurve() == null && signatureAlgorithm.getKeyLength() != null) {
            keypairGen.initialize(signatureAlgorithm.getKeyLength(), new SecureRandom());
        } else {
            if (signatureAlgorithm.getEllipticCurve() != null) {
                keypairGen.initialize(new ECGenParameterSpec(Objects.requireNonNull(signatureAlgorithm.getEllipticCurve())));
            } else {
                throw new IllegalArgumentException("EllipticCurve is null");
            }
        }
        return keypairGen;
    }
}
