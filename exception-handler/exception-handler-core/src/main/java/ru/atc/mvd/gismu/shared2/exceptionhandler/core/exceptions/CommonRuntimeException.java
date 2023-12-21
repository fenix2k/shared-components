package ru.atc.mvd.gismu.shared2.exceptionhandler.core.exceptions;

import ru.atc.mvd.gismu.shared2.exceptionhandler.core.ExceptionCode;

/**
 * Общее исключение.
 */
@SuppressWarnings("unused")
public class CommonRuntimeException extends AbstractRuntimeException {

    private static final long serialVersionUID = 1L;

    public CommonRuntimeException(ExceptionCode code, String message, String localizedMessage, Throwable ex) {
        super(code, message, localizedMessage, null, ex);
    }

    public CommonRuntimeException(ExceptionCode code, String message, String localizedMessage) {
        super(code, message, localizedMessage, null, null);
    }

    public CommonRuntimeException(ExceptionCode code, String message, String localizedMessage, Object details) {
        super(code, message, localizedMessage, details, null);
    }

    public CommonRuntimeException(ExceptionCode code, String message, Throwable ex) {
        super(code, message, null, null, ex);
    }

    public CommonRuntimeException(ExceptionCode code, String message) {
        super(code, message, null, null, null);
    }
}