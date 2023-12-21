package ru.atc.mvd.gismu.shared2.exceptionhandler.core.exceptions;

import lombok.Getter;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.CommonExceptionCodes;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.ExceptionCode;

/**
 * Абстрактный класс исключений.
 * Все остальные должны быть унаследованы от него.
 */
@Getter
@SuppressWarnings("unused")
public abstract class AbstractRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Описание ошибки. */
    private final ExceptionCode code;

    /** Код Http статуса. */
    private final int httpStatusCode;

    /** Дополнительная информация. */
    private final Object details;

    /** Сообщение об ошибке для отображения пользователю. */
    private final String localizedMessage;

    /**
     * Конструктор.
     *
     * @param code код сообщения {@link ExceptionCode}
     * @param httpStatusCode код http статуса
     * @param message сообщение об ошибке {@link String}
     * @param localizedMessage сообщение об ошибке для пользователя {@link String}
     * @param details дополнительная информация об ошибке {@link Object}
     * @param ex исключение {@link Throwable}
     */
    public AbstractRuntimeException(ExceptionCode code, int httpStatusCode,
                                    String message, String localizedMessage,
                                    Object details, Throwable ex) {
        super(message, ex);
        this.localizedMessage = localizedMessage;
        this.code = code != null ? code : CommonExceptionCodes.COMMON;
        this.httpStatusCode = httpStatusCode > 0 ? httpStatusCode : this.code.getHttpStatusCode();
        this.details = details;
    }

    public AbstractRuntimeException(ExceptionCode code, String message, String localizedMessage,
                                    Object details, Throwable ex) {
        this(code, 0, message, localizedMessage, details, ex);
    }

    public AbstractRuntimeException(ExceptionCode code, String message, String localizedMessage, Throwable ex) {
        this(code, 0, message, localizedMessage, null, ex);
    }
}
