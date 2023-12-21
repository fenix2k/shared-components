package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.provider;

import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.AsymmetricCipherAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.HmacAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.PbeSymmetricCipherAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.SignatureAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.SymmetricCipherAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.exception.CryptoProviderException;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.lang.Assert;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.provider.api.CryptoProvider;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.provider.utils.CryptoOperationErrors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * BouncyCastle CryptoProvider.
 */
@SuppressWarnings("unused")
public class CommonCryptoProvider implements CryptoProvider {

    private final String providerName;

    public CommonCryptoProvider() {
        providerName = null;
        Security.setProperty("crypto.policy", "unlimited");
    }

    @Override
    public byte[] encrypt(SymmetricCipherAlgorithm algorithm, final byte[] data, SecretKey key, AlgorithmParameterSpec paramSpec)
            throws CryptoProviderException {
        Assert.notNull(algorithm, CryptoOperationErrors.INVALID_INPUT_ALGORITHM);
        Assert.notEmpty(data, CryptoOperationErrors.INVALID_INPUT_DATA);
        Assert.notNull(key, CryptoOperationErrors.NO_CERT_PRESENT);

        // Получение идентификатора алгоритма из сертификата
//        CipherAlgorithm algorithm = CipherAlgorithm.fromString(key.getAlgorithm());
//        if (algorithm == null) {
//            throw new CryptoProviderException(CryptoOperationErrors.ALGORITHM_NOT_FOUND + ": " + key.getAlgorithm());
//        }

        try {
            Cipher cipher = Cipher.getInstance(algorithm.getIdentifier());
            if (paramSpec == null) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            }
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_ALGORITHM_NOT_FOUND, ex);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.INVALID_KEY, ex);
        } catch (NoSuchPaddingException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.EXTRACT_SIGN_ERROR, ex);
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_FORMAT_ERROR, ex);
        }
    }

    @Override
    public byte[] encrypt(AsymmetricCipherAlgorithm algorithm, final byte[] data, PublicKey publicKey)
            throws CryptoProviderException {
        Assert.notNull(algorithm, CryptoOperationErrors.INVALID_INPUT_ALGORITHM);
        Assert.notEmpty(data, CryptoOperationErrors.INVALID_INPUT_DATA);
        Assert.notNull(publicKey, CryptoOperationErrors.NO_CERT_PRESENT);

        // Получение идентификатора алгоритма из сертификата
//        CipherAlgorithm algorithm = CipherAlgorithm.fromString(publicKey.getSigAlgName());
//        if (algorithm == null) {
//            throw new CryptoProviderException(CryptoOperationErrors.ALGORITHM_NOT_FOUND + ": " + publicKey.getSigAlgName());
//        }

        try {
            Cipher cipher = Cipher.getInstance(algorithm.getIdentifier());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.ALGORITHM_NOT_FOUND, ex);
        } catch (InvalidKeyException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.INVALID_KEY, ex);
        } catch (NoSuchPaddingException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.EXTRACT_SIGN_ERROR, ex);
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_FORMAT_ERROR, ex);
        }
    }

    @Override
    public byte[] decrypt(final byte[] encryptedData, final SecretKey key, AlgorithmParameterSpec paramSpec) throws CryptoProviderException {
        Assert.notEmpty(encryptedData, CryptoOperationErrors.INVALID_INPUT_DATA);
        Assert.notNull(key, CryptoOperationErrors.NO_KEY_PRESENT);

        // Получение идентификатора алгоритма из сертификата
        SymmetricCipherAlgorithm algorithm = SymmetricCipherAlgorithm.fromString(key.getAlgorithm());
        if (algorithm == null) {
            throw new CryptoProviderException(CryptoOperationErrors.ALGORITHM_NOT_FOUND + ": " + key.getAlgorithm());
        }

        try {
            Cipher cipher = Cipher.getInstance(algorithm.getIdentifier());
            if (paramSpec == null) {
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            }
            return cipher.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.ALGORITHM_NOT_FOUND, ex);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.INVALID_KEY, ex);
        } catch (NoSuchPaddingException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.EXTRACT_SIGN_ERROR, ex);
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_FORMAT_ERROR, ex);
        }
    }

    @Override
    public byte[] decrypt(final byte[] encryptedData, final PrivateKey privateKey) throws CryptoProviderException {
        Assert.notEmpty(encryptedData, CryptoOperationErrors.INVALID_INPUT_DATA);
        Assert.notNull(privateKey, CryptoOperationErrors.NO_KEY_PRESENT);

        // Получение идентификатора алгоритма из сертификата
        AsymmetricCipherAlgorithm algorithm = AsymmetricCipherAlgorithm.fromString(privateKey.getAlgorithm());
        if (algorithm == null) {
            throw new CryptoProviderException(CryptoOperationErrors.ALGORITHM_NOT_FOUND + ": " + privateKey.getAlgorithm());
        }

        try {
            Cipher cipher = Cipher.getInstance(algorithm.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_ALGORITHM_NOT_FOUND, ex);
        } catch (InvalidKeyException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.INVALID_KEY, ex);
        } catch (NoSuchPaddingException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.EXTRACT_SIGN_ERROR, ex);
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_FORMAT_ERROR, ex);
        }
    }

    @Override
    public byte[] sign(final byte[] data, final PrivateKey privateKey, final X509Certificate publicKey,
                       final X509Certificate[] certificateChain)
            throws CryptoProviderException {
        Assert.notEmpty(data, CryptoOperationErrors.INVALID_INPUT_DATA);
        Assert.notNull(publicKey, CryptoOperationErrors.NO_CERT_PRESENT);
        Assert.notNull(privateKey, CryptoOperationErrors.NO_KEY_PRESENT);

        //List<X509Certificate> certList = new ArrayList<>();
        // TODO certificateChain not supported
//        if (certificateChain == null || certificateChain.length == 0) {
//            certList = List.of(publicKey);
//        } else {
//            certList = List.of(certificateChain);
//        }

        // Получение идентификатора алгоритма из сертификата
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.fromString(publicKey.getSigAlgName());
        if (signatureAlgorithm == null) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_ALGORITHM_NOT_FOUND + ": " + publicKey.getSigAlgName());
        }

        try {
            Signature signatureAlg = Signature.getInstance(signatureAlgorithm.getIdentifier());
            signatureAlg.initSign(privateKey);

            Cipher cipher = Cipher.getInstance(signatureAlgorithm.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            signatureAlg.update(cipher.doFinal(data));
            return signatureAlg.sign();
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_ALGORITHM_NOT_FOUND, ex);
        } catch (InvalidKeyException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.INVALID_KEY, ex);
        } catch (NoSuchPaddingException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.EXTRACT_SIGN_ERROR, ex);
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_FORMAT_ERROR, ex);
        } catch (SignatureException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_GENERATION_ERROR, ex);
        }
    }

    @Override
    public boolean verifySign(final byte[] data, final byte[] signature, final X509Certificate publicKey)
            throws CryptoProviderException {
        Assert.notEmpty(data, CryptoOperationErrors.INVALID_INPUT_DATA);
        Assert.notEmpty(signature, CryptoOperationErrors.NO_SIGN_PRESENT);
        Assert.notNull(publicKey, CryptoOperationErrors.NO_CERT_PRESENT);

        // Получение идентификатора алгоритма из сертификата
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.fromString(publicKey.getSigAlgName());
        if (signatureAlgorithm == null) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_ALGORITHM_NOT_FOUND + ": " + publicKey.getSigAlgName());
        }

        try {
            Signature signatureAlg = Signature.getInstance(signatureAlgorithm.getIdentifier());
            signatureAlg.initVerify(publicKey);
            signatureAlg.update(data);
            return signatureAlg.verify(signature);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_ALGORITHM_NOT_FOUND, ex);
        } catch (InvalidKeyException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.INVALID_KEY, ex);
        } catch (SignatureException ex) {
            throw new CryptoProviderException(CryptoOperationErrors.SIGN_GENERATION_ERROR, ex);
        }
    }

    @Override
    public boolean verifySign(final byte[] data, final byte[] signature) throws CryptoProviderException {
        return verifySign(data, signature, null);
    }

    @Override
    public KeyPair generateKeyPair(AsymmetricCipherAlgorithm algorithm) throws CryptoProviderException {
        try {
            KeyPairGenerator keygen = providerName == null ? KeyPairGenerator.getInstance(algorithm.getIdentifier()) :
                    KeyPairGenerator.getInstance(algorithm.getIdentifier(), providerName);
            return keygen.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s. Ошибка: %s", CryptoOperationErrors.ALGORITHM_NOT_FOUND,
                            algorithm.getIdentifier(), ex.getMessage()));
        } catch (NoSuchProviderException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s. Ошибка: %s",
                            CryptoOperationErrors.CRYPTO_PROVIDER_NOT_FOUND,
                            providerName, ex.getMessage()));
        }
    }

    @Override
    public KeyPair generateKeyPair(SignatureAlgorithm algorithm) throws CryptoProviderException {
        try {
            KeyPairGenerator keygen = CryptoProvider.initKeyGen(algorithm);
            return keygen.generateKeyPair();
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s. Ошибка: %s", CryptoOperationErrors.ALGORITHM_NOT_FOUND,
                            algorithm.getIdentifier(), ex.getMessage()));
        } catch (InvalidAlgorithmParameterException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s. Параметры: %s. Ошибка: %s",
                            CryptoOperationErrors.ALGORITHM_PARAMS_NOT_FOUND, algorithm.getIdentifier(),
                            algorithm.getEllipticCurve(), ex.getMessage()));
        } catch (NoSuchProviderException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s. Ошибка: %s",
                            CryptoOperationErrors.CRYPTO_PROVIDER_NOT_FOUND,
                            providerName, ex.getMessage()));
        }
    }

    @Override
    public SecretKey generateSecretKey(SymmetricCipherAlgorithm algorithm) throws CryptoProviderException {
        try {
            KeyGenerator keygen = providerName == null ? KeyGenerator.getInstance(algorithm.getIdentifier()) :
                    KeyGenerator.getInstance(algorithm.getIdentifier(), providerName);
            return keygen.generateKey();
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s. Ошибка: %s", CryptoOperationErrors.ALGORITHM_NOT_FOUND,
                            algorithm.getIdentifier(), ex.getMessage()));
        } catch (NoSuchProviderException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s. Ошибка: %s",
                            CryptoOperationErrors.CRYPTO_PROVIDER_NOT_FOUND,
                            providerName, ex.getMessage()));
        }
    }

    @Override
    public SecretKey generateSecretKey(PbeSymmetricCipherAlgorithm algorithm, String password, PBEParameterSpec paramSpec)
            throws CryptoProviderException {
        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), paramSpec.getSalt(), paramSpec.getIterationCount());
            SecretKeyFactory factory = providerName == null ? SecretKeyFactory.getInstance(algorithm.getIdentifier()) :
                    SecretKeyFactory.getInstance(algorithm.getIdentifier(), providerName);
            return factory.generateSecret(keySpec);
        } catch (NoSuchAlgorithmException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s. Ошибка: %s", CryptoOperationErrors.ALGORITHM_NOT_FOUND,
                            algorithm.getIdentifier(), ex.getMessage()));
        } catch (InvalidKeySpecException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s. Ошибка: %s", CryptoOperationErrors.INVALID_KEY,
                            algorithm.getIdentifier(), ex.getMessage()));
        } catch (NoSuchProviderException ex) {
            throw new CryptoProviderException(
                    String.format("%s: %s. Ошибка: %s",
                            CryptoOperationErrors.CRYPTO_PROVIDER_NOT_FOUND,
                            providerName, ex.getMessage()));
        }
    }

    @Override
    public SecretKey generateSecretKey(HmacAlgorithm algorithm, String secretString) {
        return new SecretKeySpec(secretString.getBytes(StandardCharsets.UTF_8), algorithm.getIdentifier());
    }

    @Override
    public X509Certificate generateSelfSignedCertificate(String alias, KeyPair keyPair, SignatureAlgorithm algorithm)
            throws CryptoProviderException {
        throw new CryptoProviderException("not supported");
    }

    @Override
    public X509Certificate generateSelfSignedCertificate(String alias, SignatureAlgorithm algorithm)
            throws CryptoProviderException {
        throw new CryptoProviderException("not supported");
    }
}
