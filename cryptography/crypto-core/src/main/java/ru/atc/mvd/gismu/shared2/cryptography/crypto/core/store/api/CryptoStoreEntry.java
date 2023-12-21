package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.api;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

/**
 * Запись хранилища.
 */
@Getter
@NoArgsConstructor
@SuppressWarnings("unused")
public class CryptoStoreEntry implements KeyStore.Entry {

    private CryptoStoreEntryTypes type;
    private PrivateKey privateKey;
    private SecretKey secretKey;
    private X509Certificate certificate;
    private X509Certificate[] certificateChain;

    public CryptoStoreEntry(SecretKey secretKey) {
        this.type = CryptoStoreEntryTypes.SKEY;
        this.secretKey = secretKey;
    }

    public CryptoStoreEntry(PrivateKey privateKey, X509Certificate certificate, X509Certificate[] certificateChain) {
        this.type = CryptoStoreEntryTypes.PKEY;
        this.privateKey = privateKey;
        this.certificate = certificate;
        this.certificateChain = certificateChain;
    }

    public CryptoStoreEntry(X509Certificate certificate) {
        this.type = CryptoStoreEntryTypes.CERT;
        this.certificate = certificate;
    }

    /**
     * Установить {@link SecretKey}.
     *
     * @param secretKey {@link SecretKey}
     */
    protected void setSecretKey(SecretKey secretKey) {
        this.type = CryptoStoreEntryTypes.SKEY;
        this.secretKey = secretKey;
    }

    /**
     * Установить {@link PrivateKey}.
     *
     * @param privateKey {@link PrivateKey}
     * @param certificate {@link X509Certificate}
     * @param certificateChain  {@link X509Certificate}
     */
    protected void setPrivateKey(PrivateKey privateKey, X509Certificate certificate, X509Certificate[] certificateChain) {
        this.type = CryptoStoreEntryTypes.PKEY;
        this.privateKey = privateKey;
        this.certificate = certificate;
        this.certificateChain = certificateChain;
    }

    /**
     * Установить {@link X509Certificate}.
     *
     * @param certificate {@link X509Certificate}
     */
    protected void setCertificate(X509Certificate certificate) {
        this.type = CryptoStoreEntryTypes.CERT;
        this.certificate = certificate;
    }
}
