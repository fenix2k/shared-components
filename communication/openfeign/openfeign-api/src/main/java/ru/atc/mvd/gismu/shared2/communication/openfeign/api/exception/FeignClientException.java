package ru.atc.mvd.gismu.shared2.communication.openfeign.api.exception;

import lombok.Getter;

/**
 * Исключение feign.
 */
@Getter
@SuppressWarnings("unused")
public class FeignClientException extends RuntimeException {

    private final ExceptionCodes exceptionCode;

    public FeignClientException(ExceptionCodes exceptionCode, String message, Throwable ex) {
        super(message, ex);
        this.exceptionCode = exceptionCode;
    }

    public FeignClientException(ExceptionCodes exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }
}
