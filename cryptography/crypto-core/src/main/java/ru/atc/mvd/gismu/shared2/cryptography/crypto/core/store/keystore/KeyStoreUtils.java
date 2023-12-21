package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.keystore;

import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.exception.CryptoStoreException;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для работы с KeyStore.
 */
@SuppressWarnings("unused")
public class KeyStoreUtils {

    private static final String DEFAULT_KEYSTORE_TYPE = "JKS";
    private static final String PROVIDER_NAME;

    static {
        PROVIDER_NAME = "default";
        Security.setProperty("crypto.policy", "unlimited");
    }

    private static String getEntryType(KeyStore keyStore, String alias) {
        String type = "unknown";
        try {
            if (!keyStore.containsAlias(alias)) {
                return null;
            } else if (keyStore.isKeyEntry(alias)) {
                return "key";
            } else if (keyStore.isCertificateEntry(alias)) {
                return "cert";
            }
            return type;
        } catch (KeyStoreException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка доступа к keystore. Ошибка: %s", ex), ex);
        }
    }

    /**
     * Возвращает KeyStore.Entry.
     *
     * @param privateKey  privateKey
     * @param certificate certificate
     * @return KeyStore.Entry
     */
    public static KeyStore.Entry buildEntry(PrivateKey privateKey, Certificate certificate) {
        return new KeyStore.PrivateKeyEntry(
                privateKey,
                new Certificate[]{certificate}
        );
    }

    /**
     * Возвращает KeyStore.Entry.
     *
     * @param privateKey   privateKey
     * @param certificates certificates
     * @return KeyStore.Entry
     */
    public static KeyStore.Entry buildEntry(PrivateKey privateKey, Certificate[] certificates) {
        return new KeyStore.PrivateKeyEntry(
                privateKey,
                certificates
        );
    }

    /**
     * Возвращает KeyStore.Entry.
     *
     * @param secretKey secretKey
     * @return KeyStore.Entry
     */
    public static KeyStore.Entry buildEntry(SecretKey secretKey) {
        return new KeyStore.SecretKeyEntry(secretKey);
    }

    /**
     * Возвращает KeyStore.Entry.
     *
     * @param certificate certificate
     * @return KeyStore.Entry
     */
    public static KeyStore.Entry buildEntry(Certificate certificate) {
        return new KeyStore.TrustedCertificateEntry(certificate);
    }

    /**
     * Загружает и возвращает KeyStore.
     *
     * @param fis            fis
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @return KeyStore
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static synchronized KeyStore loadKeyStore(FileInputStream fis, String keyStorePasswd,
                                                     String keyStoreType)
            throws CryptoStoreException {
        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType == null ? DEFAULT_KEYSTORE_TYPE : keyStoreType, PROVIDER_NAME);
            keyStore.load(fis, keyStorePasswd.toCharArray());
            return keyStore;
        } catch (IOException ex) {
            throw new CryptoStoreException(
                    String.format("не удалось загрузить keystore из текущего потока. Ошибка: %s", ex), ex);
        } catch (KeyStoreException | NoSuchAlgorithmException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка доступа к keystore, type %s. Ошибка: %s", keyStoreType, ex), ex);
        } catch (CertificateException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка извлечения сертификата из keystore, type %s. Ошибка: %s",
                            keyStoreType, ex), ex);
        } catch (NoSuchProviderException ex) {
            throw new CryptoStoreException(
                    String.format("криптопровайдер не найден %s. Ошибка: %s",
                            PROVIDER_NAME, ex), ex);
        }
    }

    /**
     * Загружает и возвращает KeyStore.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @return KeyStore
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static synchronized KeyStore loadKeyStore(Path keyStorePath, String keyStorePasswd,
                                                     String keyStoreType) throws CryptoStoreException {
        try (FileInputStream fis = new FileInputStream(keyStorePath.toString())) {
            return loadKeyStore(fis, keyStorePasswd, keyStoreType);
        } catch (IOException ex) {
            throw new CryptoStoreException(
                    String.format("не удалось открыть keystore: %s. Ошибка: %s", keyStorePath, ex), ex);
        }
    }

    /**
     * Загружает и возвращает KeyStore.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @return KeyStore
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static synchronized KeyStore loadClasspathKeyStore(Path keyStorePath, String keyStorePasswd,
                                                              String keyStoreType) throws CryptoStoreException {
        Path path;
        try {
            path = Paths.get(ClassLoader.getSystemResource(keyStorePath.toString()).toURI());
        } catch (URISyntaxException ex) {
            throw new CryptoStoreException(
                    String.format("не удалось открыть keystore: %s. Ошибка: %s", keyStorePath, ex), ex);
        }

        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            return loadKeyStore(fis, keyStorePasswd, keyStoreType);
        } catch (IOException ex) {
            throw new CryptoStoreException(
                    String.format("не удалось открыть keystore: %s. Ошибка: %s", keyStorePath, ex), ex);
        }
    }

    /**
     * Создаёт новый keystore по указанному пути.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @return KeyStore
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static synchronized KeyStore createNewKeyStore(String keyStorePath, String keyStorePasswd,
                                                          String keyStoreType)
            throws CryptoStoreException {
        try (FileOutputStream fos = new FileOutputStream(keyStorePath)) {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType == null ? DEFAULT_KEYSTORE_TYPE : keyStoreType, PROVIDER_NAME);
            keyStore.load(null, null);
            keyStore.store(fos, keyStorePasswd.toCharArray());
            return keyStore;
        } catch (IOException ex) {
            throw new CryptoStoreException(
                    String.format("не удалось открыть keystore: %s. Ошибка: %s", keyStorePath, ex), ex);
        } catch (KeyStoreException | NoSuchAlgorithmException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка доступа к keystore: %s. Ошибка: %s", keyStorePath, ex), ex);
        } catch (CertificateException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка извлечения сертификата из keystore %s. Ошибка: %s",
                            keyStorePath, ex), ex);
        } catch (NoSuchProviderException ex) {
            throw new CryptoStoreException(
                    String.format("криптопровайдер не найден %s. Ошибка: %s",
                            PROVIDER_NAME, ex), ex);
        }
    }

    /**
     * Создаёт новый keystore по указанному пути.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @return KeyStore
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static synchronized KeyStore createNewKeyStore(Path keyStorePath, String keyStorePasswd,
                                                          String keyStoreType)
            throws CryptoStoreException {
        return createNewKeyStore(keyStorePath.toString(), keyStorePasswd, keyStoreType);
    }

    /**
     * Извлекает из keystore KeyStore.Entry по его alias.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @param entryAlias     entryAlias
     * @param entryPasswd    entryPasswd
     * @return KeyStore.Entry
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static KeyStore.Entry getEntry(Path keyStorePath, String keyStorePasswd,
                                          String keyStoreType,
                                          String entryAlias, String entryPasswd)
            throws CryptoStoreException {
        KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePasswd, keyStoreType);
        return getEntry(keyStore, entryAlias, entryPasswd);
    }

    /**
     * Извлекает из keystore KeyStore.Entry по его alias.
     *
     * @param keyStore    keyStore
     * @param alias       alias
     * @param entryPasswd entryPasswd
     * @return KeyStore.Entry
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static KeyStore.Entry getEntry(KeyStore keyStore,
                                          String alias, String entryPasswd)
            throws CryptoStoreException {
        try {
            return keyStore.getEntry(alias, new KeyStore.PasswordProtection(entryPasswd.toCharArray()));
        } catch (KeyStoreException | NoSuchAlgorithmException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка доступа к keystore. Ошибка: %s", ex), ex);
        } catch (UnrecoverableEntryException ex) {
            throw new CryptoStoreException(
                    "ошибка доступа к keystore: неверный пароль", ex);
        }
    }

    /**
     * Извлекает из keystore KeyStore.Entry по его alias.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @param alias          alias
     * @return Certificate[]
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static Certificate[] getCertificateChain(Path keyStorePath, String keyStorePasswd,
                                                    String keyStoreType,
                                                    String alias)
            throws CryptoStoreException {
        KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePasswd, keyStoreType);
        return getCertificateChain(keyStore, alias);
    }

    /**
     * Извлекает из keystore цепочку сертификатов по его alias.
     *
     * @param keyStore keyStore
     * @param alias    alias
     * @return Certificate[]
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static Certificate[] getCertificateChain(KeyStore keyStore,
                                                    String alias)
            throws CryptoStoreException {
        try {
            return keyStore.getCertificateChain(alias);
        } catch (KeyStoreException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка доступа к keystore. Ошибка: %s", ex), ex);
        }
    }

    /**
     * Извлекает из keystore KeyStore.Entry по его alias.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @param alias          alias
     * @return Certificate
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static Certificate getCertificate(Path keyStorePath, String keyStorePasswd,
                                             String keyStoreType,
                                             String alias)
            throws CryptoStoreException {
        KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePasswd, keyStoreType);
        return getCertificate(keyStore, alias);
    }

    /**
     * Извлекает из keystore сертификат по его alias.
     *
     * @param keyStore keyStore
     * @param alias    alias
     * @return Certificate
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static Certificate getCertificate(KeyStore keyStore, String alias)
            throws CryptoStoreException {
        try {
            return keyStore.getCertificate(alias);
        } catch (KeyStoreException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка доступа к keystore. Ошибка: %s", ex), ex);
        }
    }

    /**
     * Извлекает из keystore Key по его keyAlias.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @param keyAlias       keyAlias
     * @param keyPasswd      keyPasswd
     * @return Certificate
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static Key getKey(Path keyStorePath, String keyStorePasswd,
                             String keyStoreType,
                             String keyAlias, String keyPasswd)
            throws CryptoStoreException {
        KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePasswd, keyStoreType);
        return getKey(keyStore, keyAlias, keyPasswd);
    }

    /**
     * Извлекает из keystore Key по его keyAlias.
     *
     * @param keyStore  keyStore
     * @param keyPasswd keyPasswd
     * @return Certificate
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static Key getKey(KeyStore keyStore,
                             String keyAlias, String keyPasswd)
            throws CryptoStoreException {
        try {
            return keyStore.getKey(keyAlias, keyPasswd.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка доступа к keystore. Ошибка: %s", ex), ex);
        }
    }

    /**
     * Сохраняет в keystore ключи и сертификат под идентификатором alias.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @param alias          alias
     * @param entry          entry
     * @param entryPasswd    entryPasswd
     * @return KeyStore
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static synchronized KeyStore saveKeyStoreEntry(Path keyStorePath, String keyStorePasswd,
                                                          String keyStoreType,
                                                          String alias, KeyStore.Entry entry,
                                                          String entryPasswd)
            throws CryptoStoreException {
        try {
            if (!Files.isRegularFile(keyStorePath)) {
                throw new CryptoStoreException(String.format("Keystore не найден: %s", keyStorePath));
            }
            KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePasswd, keyStoreType);
            keyStore.deleteEntry(alias);
            keyStore.setEntry(
                    alias,
                    entry,
                    new KeyStore.PasswordProtection(entryPasswd.toCharArray())
            );
            FileOutputStream fos = new FileOutputStream(keyStorePath.toString());
            keyStore.store(fos, keyStorePasswd.toCharArray());
            fos.close();
            return keyStore;
        } catch (IOException ex) {
            throw new CryptoStoreException(
                    String.format("не удалось открыть keystore: %s. Ошибка: %s", keyStorePath, ex), ex);
        } catch (KeyStoreException | NoSuchAlgorithmException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка доступа к keystore: %s. Ошибка: %s", keyStorePath, ex), ex);
        } catch (CertificateException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка добавления сертификата в keystore: %s. Ошибка: %s",
                            keyStorePath, ex), ex);
        }
    }

    /**
     * Сохраняет в keystore ключи и сертификат под идентификатором alias.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @param alias          alias
     * @return KeyStore
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static synchronized KeyStore removeKeyStoreEntry(Path keyStorePath, String keyStorePasswd,
                                                            String keyStoreType,
                                                            String alias)
            throws CryptoStoreException {
        try {
            KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePasswd, keyStoreType);
            keyStore.deleteEntry(alias);
            FileOutputStream fos = new FileOutputStream(keyStorePath.toString());
            keyStore.store(fos, keyStorePasswd.toCharArray());
            fos.close();
            return keyStore;
        } catch (IOException ex) {
            throw new CryptoStoreException(
                    String.format("не удалось открыть keystore: %s. Ошибка: %s", keyStorePath, ex), ex);
        } catch (KeyStoreException | NoSuchAlgorithmException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка доступа к keystore: %s. Ошибка: %s", keyStorePath, ex), ex);
        } catch (CertificateException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка добавления сертификата в keystore: %s. Ошибка: %s",
                            keyStorePath, ex), ex);
        }
    }

    /**
     * Возвращает список содержимого keystore.
     *
     * @param keyStorePath   keyStorePath
     * @param keyStorePasswd keyStorePasswd
     * @param keyStoreType   keyStoreType
     * @return Map[Alias, Type]
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static Map<String, String> getAliases(Path keyStorePath, String keyStorePasswd,
                                                 String keyStoreType)
            throws CryptoStoreException {
        KeyStore keyStore = loadKeyStore(keyStorePath, keyStorePasswd, keyStoreType);
        return getAliases(keyStore);
    }

    /**
     * Возвращает список содержимого keystore.
     *
     * @param keyStore keyStore
     * @return Map[Alias, Type]
     * @throws CryptoStoreException KeyStore Operation Exception
     */
    public static Map<String, String> getAliases(KeyStore keyStore)
            throws CryptoStoreException {
        Map<String, String> aliases = new HashMap<>();
        try {
            Enumeration<String> enumeration = keyStore.aliases();
            while (enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                String type = getEntryType(keyStore, alias);
                aliases.put(alias, type);
            }
            return aliases;
        } catch (KeyStoreException ex) {
            throw new CryptoStoreException(
                    String.format("ошибка доступа к keystore. Ошибка: %s", ex), ex);
        }
    }
}
