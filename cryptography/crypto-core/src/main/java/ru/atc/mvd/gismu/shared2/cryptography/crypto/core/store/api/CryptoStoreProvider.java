package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.api;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Map;

/**
 * Интерфейс для управления хранилищем ключей.
 */
@SuppressWarnings("unused")
public interface CryptoStoreProvider {

    /**
     * Сохранить ключ и сертификат под идентификатором alias.
     *
     * @param alias       alias
     * @param privateKey  {@link PrivateKey}
     * @param cert        {@link Certificate}
     * @param entryPasswd entryPasswd
     */
    void save(String alias, PrivateKey privateKey, Certificate cert, String entryPasswd);

    /**
     * Сохранить ключ и сертификаты под идентификатором alias.
     *
     * @param alias       alias
     * @param privateKey  {@link PrivateKey}
     * @param certs       {@link Certificate}[]
     * @param entryPasswd entryPasswd
     */
    void save(String alias, PrivateKey privateKey, Certificate[] certs, String entryPasswd);

    /**
     * Сохранить secretKey под идентификатором alias.
     *
     * @param alias       alias
     * @param secretKey   {@link SecretKey}
     * @param entryPasswd entryPasswd
     */
    void save(String alias, SecretKey secretKey, String entryPasswd);

    /**
     * Сохранить сертификат под идентификатором alias.
     *
     * @param alias       alias
     * @param certificate {@link Certificate}
     * @param entryPasswd entryPasswd
     */
    void save(String alias, Certificate certificate, String entryPasswd);

    /**
     * Удалить запись с указанным alias.
     *
     * @param alias alias
     */
    void remove(String alias);

    /**
     * Извлечь запись по его alias.
     *
     * @param alias       alias
     * @param entryPasswd entryPasswd
     * @return {@link KeyStore.Entry}
     */
    CryptoStoreEntry getEntry(String alias, String entryPasswd);

    /**
     * Извлечь ключ Key по его alias.
     *
     * @param alias       alias
     * @param entryPasswd entryPasswd
     * @return {@link Key}
     */
    Key getKey(String alias, String entryPasswd);

    /**
     * Извлечь сертификат Certificate по его alias.
     *
     * @param alias alias
     * @return {@link Certificate}
     */
    Certificate getCertificate(String alias);

    /**
     * Извлечь цепочку сертификатов Certificate[] по его alias.
     *
     * @param alias alias
     * @return {@link Certificate}[]
     */
    Certificate[] getCertificateChain(String alias);

    /**
     * Получить список записей хранилища в виде мапы[alias, тип].
     *
     * @return {@link Map}<{@link String}, {@link String}>
     */
    Map<String, String> getAliases();
}
