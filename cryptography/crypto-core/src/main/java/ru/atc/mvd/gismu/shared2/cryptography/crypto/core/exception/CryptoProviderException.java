package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.exception;

/**
 * Исключение при неверном поисковом запросе.
 */
@SuppressWarnings("unused")
public class CryptoProviderException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private static final String COMMON_MESSAGE = "Ошибка криптографической операции: ";

    public CryptoProviderException(String message) {
        super(COMMON_MESSAGE + message);
    }

    public CryptoProviderException(String message, Throwable cause) {
        super(COMMON_MESSAGE + message, cause);
    }
}
