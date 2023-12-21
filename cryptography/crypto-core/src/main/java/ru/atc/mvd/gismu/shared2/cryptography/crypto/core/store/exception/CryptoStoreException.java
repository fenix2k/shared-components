package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.store.exception;

/**
 * Исключение при ошибке работы с CryptoStore.
 */
public class CryptoStoreException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String COMMON_MESSAGE = "Ошибка операции с CryptoStore: ";

    public CryptoStoreException(String message) {
        super(COMMON_MESSAGE + message);
    }

    public CryptoStoreException(String message, Throwable cause) {
        super(COMMON_MESSAGE + message, cause);
    }
}
