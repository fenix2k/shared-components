package ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers;

import ru.atc.mvd.gismu.shared2.exceptionhandler.core.dto.CommonErrorMessageDto;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.mapper.ErrorMessageMapper;

/**
 * Интерфейс обработчиков сообщений об ошибках.
 */
@SuppressWarnings("unused")
public interface ExceptionHandler {

    /**
     * Установить класс исключения. Обязательно для переопределения!!
     *
     * @return {@link Class}<{@link Exception}>
     */
    Class<? extends Exception> exceptionClass();

    /**
     * Условие при котором обработчик применим.
     *
     * @param ex исключение {@link Exception}
     * @return boolean
     */
    boolean accept(Exception ex);

    /**
     * Получить сообщение об ошибке на основе исключение и маппера.
     *
     * @param ex исключение {@link Exception}
     * @param errorMessageMapper маппер {@link ErrorMessageMapper}
     * @return {@link CommonErrorMessageDto}
     */
    CommonErrorMessageDto getErrorMessage(Exception ex, ErrorMessageMapper errorMessageMapper);

    /**
     * Выполняется перед построением сообщения об ошибке.
     *
     * @param ex исключение {@link Exception}
     */
    void beforeBuildErrorMessage(Exception ex);

    /**
     * Выполняется после построения сообщения об ошибке.
     *
     * @param errorMessage сообщение об ошибке {@link CommonErrorMessageDto}
     */
    void afterBuildErrorMessage(CommonErrorMessageDto errorMessage);
}
