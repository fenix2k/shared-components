package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception;

import lombok.Getter;

/**
 * Исключение при поиске данных.
 */
@Getter
@SuppressWarnings("unused")
public class JpaDataSearchException extends RuntimeException {

    private final ExceptionCodes exceptionCode;
    private final Object details;

    public JpaDataSearchException(ExceptionCodes exceptionCode, String message, Object details, Throwable ex) {
        super(message, ex);
        this.exceptionCode = exceptionCode;
        this.details = details;
    }

    public JpaDataSearchException(ExceptionCodes exceptionCode, String message) {
        this(exceptionCode, message, null, null);
    }

    public JpaDataSearchException(ExceptionCodes exceptionCode, String message, Object details) {
        this(exceptionCode, message, details, null);
    }
}
