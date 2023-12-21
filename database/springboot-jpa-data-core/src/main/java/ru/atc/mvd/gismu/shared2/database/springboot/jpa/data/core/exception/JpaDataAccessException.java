package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception;

import lombok.Getter;

/**
 * Исключение при запросе данных.
 */
@Getter
@SuppressWarnings("unused")
public class JpaDataAccessException extends RuntimeException {

    private final ExceptionCodes exceptionCode;

    public JpaDataAccessException(ExceptionCodes exceptionCode, String message, Throwable ex) {
        super(message, ex);
        this.exceptionCode = exceptionCode;
    }

    public JpaDataAccessException(ExceptionCodes exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
