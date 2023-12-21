package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.keystore;

import lombok.extern.slf4j.Slf4j;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.api.CryptoStoreEntry;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.api.CryptoStoreProvider;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.exception.CryptoStoreException;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.keystore.configuration.AbstractKeyStoreConfigProperties;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Map;

/**
 * Провайдер для управления хранилищем ключей.
 */
@Slf4j
@SuppressWarnings("unused")
public class KeystoreProvider implements CryptoStoreProvider {

    private final AbstractKeyStoreConfigProperties properties;
    private KeyStore keystore;

    public KeystoreProvider(AbstractKeyStoreConfigProperties properties) {
        this.properties = properties;
        initKeyStore();
    }

    /**
     * Инициализация keystore.
     */
    protected void initKeyStore() {
        if (properties.getPath() == null || properties.getPath().toString().trim().isEmpty()) {
            throw new CryptoStoreException("Путь к keystore не указан");
        }

        if (!Files.isRegularFile(properties.getPath())) {
            if (properties.getAutocreate()) {
                KeyStore defaultKeystore = getDefaultKeystore(properties.getPath());
                if (defaultKeystore != null) {
                    keystore = defaultKeystore;
                    log.info("Найден keystore по-умолчанию. Путь: classpath://resource/{}. Тип: {}",
                            properties.getPath(), properties.getType());
                } else {
                    keystore = KeyStoreUtils.createNewKeyStore(
                            properties.getPath(),
                            properties.getPassword(),
                            properties.getType()
                    );
                    log.info("Создание keystore. Путь: {}. Тип: {}", properties.getPath(), properties.getType());
                }
            } else {
                throw new CryptoStoreException(String.format("Не удалось найти keystore: %s", properties.getPath()));
            }
        }
        if (keystore == null) {
            keystore = KeyStoreUtils.loadKeyStore(properties.getPath(), properties.getPassword(), properties.getType());
        }
        log.info("Keystore загружен. Путь: {}. Тип: {}", properties.getPath(), properties.getType());
    }

    /**
     * Ищет keystore в ресурсах, копирует его по указанному пути и загружает.
     *
     * @param keyStorePath  keyStorePath
     * @return KeyStore
     */
    private KeyStore getDefaultKeystore(Path keyStorePath) {
        Path fileName = keyStorePath.getFileName();
        if (fileName == null) {
            return null;
        }

        try {
            Path path = Paths.get(ClassLoader.getSystemResource(keyStorePath.toString()).toURI());
            if (Files.isRegularFile(path)) {
                byte[] bytes = Files.readAllBytes(path);
                Files.write(keyStorePath, bytes, StandardOpenOption.CREATE);
                return KeyStoreUtils.loadKeyStore(keyStorePath, properties.getPassword(), properties.getType());
            }
        } catch (IOException | URISyntaxException ex) {
            log.error("Не удалось открыть default keystore: {}. Причина: {}",
                    keyStorePath.getFileName(), ex.getLocalizedMessage(), ex);
        }

        return null;
    }

    /**
     * Сохраняет данные в keystore и загружает в память.
     *
     * @param alias       alias
     * @param entry       {@link KeyStore.Entry}
     * @param entryPasswd entryPasswd
     */
    private synchronized void save(String alias, KeyStore.Entry entry, String entryPasswd) {
        keystore = KeyStoreUtils
                .saveKeyStoreEntry(properties.getPath(), properties.getPassword(), properties.getType(), alias, entry, entryPasswd);
    }

    @Override
    public void save(String alias, PrivateKey privateKey, Certificate cert, String entryPasswd) {
        save(alias, KeyStoreUtils.buildEntry(privateKey, cert), entryPasswd);
    }

    @Override
    public void save(String alias, PrivateKey privateKey, Certificate[] certs, String entryPasswd) {
        save(alias, KeyStoreUtils.buildEntry(privateKey, certs), entryPasswd);
    }

    @Override
    public void save(String alias, SecretKey secretKey, String entryPasswd) {
        save(alias, KeyStoreUtils.buildEntry(secretKey), entryPasswd);
    }

    @Override
    public void save(String alias, Certificate certificate, String entryPasswd) {
        save(alias, KeyStoreUtils.buildEntry(certificate), entryPasswd);
    }

    @Override
    public void remove(String alias) {
        keystore = KeyStoreUtils
                .removeKeyStoreEntry(properties.getPath(), properties.getPassword(), properties.getType(), alias);
    }

    @Override
    public CryptoStoreEntry getEntry(String alias, String entryPasswd) {
        return new KeystoreEntry(KeyStoreUtils.getEntry(keystore, alias, entryPasswd));
    }

    @Override
    public Key getKey(String alias, String entryPasswd) {
        return KeyStoreUtils.getKey(keystore, alias, entryPasswd);
    }

    @Override
    public Certificate getCertificate(String alias) {
        return KeyStoreUtils.getCertificate(keystore, alias);
    }

    @Override
    public Certificate[] getCertificateChain(String alias) {
        return KeyStoreUtils.getCertificateChain(keystore, alias);
    }

    @Override
    public Map<String, String> getAliases() {
        return KeyStoreUtils.getAliases(keystore);
    }
}
