package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.keystore;

import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.api.CryptoStoreEntry;
import ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.exception.CryptoStoreException;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

/**
 * Запись в keystore.
 */
public class KeystoreEntry extends CryptoStoreEntry {

    public KeystoreEntry(KeyStore.Entry entry) {
        super();
        if (entry instanceof KeyStore.SecretKeyEntry) {
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) entry;
            this.setSecretKey(secretKeyEntry.getSecretKey());
        } else if (entry instanceof KeyStore.PrivateKeyEntry) {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) entry;
            this.setPrivateKey(
                    privateKeyEntry.getPrivateKey(),
                    (X509Certificate) privateKeyEntry.getCertificate(),
                    (X509Certificate[]) privateKeyEntry.getCertificateChain()
            );
        } else if (entry instanceof KeyStore.TrustedCertificateEntry) {
            KeyStore.TrustedCertificateEntry certificateEntry = (KeyStore.TrustedCertificateEntry) entry;
            this.setCertificate((X509Certificate) certificateEntry.getTrustedCertificate());
        }
        throw new CryptoStoreException("Не удалось загрузить ключ");
    }
}
