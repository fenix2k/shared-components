package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.AsymmetricCipherAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.HashAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.HmacAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.PbeSymmetricCipherAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.algorithm.SymmetricCipherAlgorithm;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.provider.api.CryptoProvider;

import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.Provider;
import java.security.Security;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class CommonCryptoProviderTest {

    private static final String SECRET_STRING = "Secret test data 2023. Секретная информация.";
    private final CryptoProvider cryptoProvider = new CommonCryptoProvider();

    @Test
    void printAvailableAlgs() {
        Set<String> types = new HashSet<>();
        types.add("KeyPairGenerator");
        types.add("Signature");
        types.add("MessageDigest");
        types.add("Mac");
        types.add("Cipher");

        for (Provider provider : Security.getProviders()) {
            System.out.println("Provider = " + provider.getName());
            Map<String, List<Provider.Service>> services = provider.getServices().stream()
                    .filter(s -> types.contains(s.getType()))
                    .sorted(Comparator.comparing(Provider.Service::getAlgorithm))
                    .collect(Collectors.groupingBy(Provider.Service::getType));

            for (String type : services.keySet()) {
                System.out.printf("  Type = %s%n", type);
                for (Provider.Service service : services.get(type)) {
                    System.out.printf("    %s%n", service.getAlgorithm());
                }
            }
        }
    }

    @Test
    void hash1() {
        testHash(HashAlgorithm.sha1, SECRET_STRING);
    }

    @Test
    void hash() {
        testHash(HashAlgorithm.md2, SECRET_STRING);
        testHash(HashAlgorithm.md5, SECRET_STRING);
        testHash(HashAlgorithm.sha1, SECRET_STRING);
        testHash(HashAlgorithm.sha224, SECRET_STRING);
        testHash(HashAlgorithm.sha256, SECRET_STRING);
        testHash(HashAlgorithm.sha384, SECRET_STRING);
        testHash(HashAlgorithm.sha512, SECRET_STRING);
        testHash(HashAlgorithm.sha3_224, SECRET_STRING);
        testHash(HashAlgorithm.sha3_256, SECRET_STRING);
        testHash(HashAlgorithm.sha3_384, SECRET_STRING);
        testHash(HashAlgorithm.sha3_512, SECRET_STRING);

        //testHash(HashAlgorithm.md4, testData);
        //testHash(HashAlgorithm.sha512_224, testData);
        //testHash(HashAlgorithm.sha512_256, testData);
        //testHash(HashAlgorithm.shake128, testData);
        //testHash(HashAlgorithm.shake256, testData);
        //testHash(HashAlgorithm.ripemd128, testData);
        //testHash(HashAlgorithm.ripemd160, testData);
        //testHash(HashAlgorithm.ripemd256, testData);
        //testHash(HashAlgorithm.sm3, testData);
    }

    @Test
    void hmac() {
        testHmac(HmacAlgorithm.hmacMd5, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha1, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha224, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha256, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha384, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha512, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha512_224, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha512_256, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha3_224, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha3_256, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha3_384, SECRET_STRING);
        testHmac(HmacAlgorithm.hmacSha3_512, SECRET_STRING);
    }

    @Test
    void generateKeyPair() {
    }

    @Test
    void generateSecretKey() {
    }

    @Test
    void testGenerateSecretKey() {
        SecretKey secretKey = cryptoProvider.generateSecretKey(SymmetricCipherAlgorithm.aes);
        Assertions.assertNotNull(secretKey);
    }

    @Test
    void generateSelfSignedCertificate() {
    }

    @Test
    void testGenerateSelfSignedCertificate() {
    }

    @Test
    void test_symmetric_encrypt_and_decrypt() {
        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.blowfish, SECRET_STRING);
        //testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.rsa, testData);

        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes, SECRET_STRING);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes_gcm, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes_kw, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes_kwp, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes128_gcm, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes128_kw, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes128_kwp, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes192_gcm, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes192_kw, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes192_kwp, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes256_gcm, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes256_kw, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.aes256_kwp, testData);

//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.pbe_hmacSha1_aes128, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.pbe_hmacSha1_aes256, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.pbe_hmacSha256_aes128, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.pbe_hmacSha512_aes256, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.pbe_hmacSha256_aes128, testData);
//        testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm.pbe_hmacSha512_aes256, testData);

        testPbeSymmetricEncryptAndDecrypt(PbeSymmetricCipherAlgorithm.pbe_md5_des, SECRET_STRING);
        testPbeSymmetricEncryptAndDecrypt(PbeSymmetricCipherAlgorithm.pbe_md5_3des, SECRET_STRING);
    }

    @Test
    void test_asymmetric_encrypt_and_decrypt() {
        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.rsa, SECRET_STRING);

//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes_gcm, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes_kw, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes_kwp, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes128_gcm, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes128_kw, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes128_kwp, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes192_gcm, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes192_kw, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes192_kwp, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes256_gcm, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes256_kw, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.aes256_kwp, testData);
//
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.pbe_hmacSha1_aes128, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.pbe_hmacSha1_aes256, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.pbe_hmacSha256_aes128, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.pbe_hmacSha512_aes256, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.pbe_hmacSha256_aes128, testData);
//        testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm.pbe_hmacSha512_aes256, testData);

    }

    @Test
    void sign() {
    }

    @Test
    void verifySign() {
    }

    @Test
    void testVerifySign() {
    }

    private void testHash(HashAlgorithm hashAlgorithm, String testData) {
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
        byte[] hash0 = cryptoProvider.hash(hashAlgorithm, testDataBytes);
        byte[] hash1 = cryptoProvider.hash(hashAlgorithm, testDataBytes);
        Assertions.assertNotNull(hash0);
        Assertions.assertTrue(hash0.length > 0);
        Assertions.assertArrayEquals(hash0, hash1);
        System.out.println("Test HashAlgorithm " + hashAlgorithm.getIdentifier() + " success");
    }

    private void testHmac(HmacAlgorithm hmacAlgorithm, String testData) {
        String secretKeyString = "secret string";
        SecretKey secretKey = cryptoProvider.generateSecretKey(hmacAlgorithm, secretKeyString);
        byte[] encryptedData0 = cryptoProvider.hmac(testData.getBytes(StandardCharsets.UTF_8), secretKey);
        byte[] encryptedData1 = cryptoProvider.hmac(testData.getBytes(StandardCharsets.UTF_8), secretKey);
        Assertions.assertNotNull(encryptedData0);
        Assertions.assertTrue(encryptedData0.length > 0);
        Assertions.assertArrayEquals(encryptedData0, encryptedData1);
        System.out.println("Test HmacAlgorithm " + hmacAlgorithm.getIdentifier() + " success");
    }

    private void testSymmetricEncryptAndDecrypt(SymmetricCipherAlgorithm algorithm, String testData) {
        SecretKey secretKey = cryptoProvider.generateSecretKey(algorithm);
        byte[] encryptedData = cryptoProvider.encrypt(algorithm, testData.getBytes(StandardCharsets.UTF_8), secretKey, null);
        Assertions.assertNotNull(encryptedData);
        byte[] decryptedData = cryptoProvider.decrypt(encryptedData, secretKey, null);
        Assertions.assertEquals(testData, new String(decryptedData, StandardCharsets.UTF_8));
        System.out.println("Test SymmetricCipherAlgorithm " + algorithm.getIdentifier() + " success");
    }

    private void testPbeSymmetricEncryptAndDecrypt(PbeSymmetricCipherAlgorithm algorithm, String testData) {
        PBEParameterSpec paramSpec = new PBEParameterSpec(new byte[8], 1);
        SecretKey secretKey = cryptoProvider.generateSecretKey(algorithm, "passwd", paramSpec);
        byte[] encryptedData = cryptoProvider.encrypt(algorithm, testData.getBytes(StandardCharsets.UTF_8), secretKey, paramSpec);
        Assertions.assertNotNull(encryptedData);
        byte[] decryptedData = cryptoProvider.decrypt(encryptedData, secretKey, paramSpec);
        Assertions.assertEquals(testData, new String(decryptedData, StandardCharsets.UTF_8));
        System.out.println("Test SymmetricCipherAlgorithm " + algorithm.getIdentifier() + " success");
    }

    private void testAsymmetricEncryptAndDecrypt(AsymmetricCipherAlgorithm algorithm, String testData) {
        KeyPair keyPair = cryptoProvider.generateKeyPair(algorithm);
        byte[] encryptedData = cryptoProvider.encrypt(algorithm, testData.getBytes(StandardCharsets.UTF_8), keyPair.getPublic());
        Assertions.assertNotNull(encryptedData);
        byte[] decryptedData = cryptoProvider.decrypt(encryptedData, keyPair.getPrivate());
        Assertions.assertEquals(testData, new String(decryptedData, StandardCharsets.UTF_8));
        System.out.println("Test AsymmetricCipherAlgorithm " + algorithm.getIdentifier() + " success");
    }
}