package ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.dto.CommonErrorMessageDto;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.exceptions.AbstractRuntimeException;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.mapper.ErrorMessageMapper;

/**
 * Абстрактный обработчик исключений с логикой по-умолчанию.
 * От него наследуются все кастомные обработчики.
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractExceptionHandler implements ExceptionHandler {

    @Override
    public abstract Class<? extends Exception> exceptionClass();

    @Override
    public final boolean accept(Exception ex) {
        Class<? extends Exception> exceptionClass = this.exceptionClass();
        if (exceptionClass == null) {
            throw new IllegalArgumentException("ExceptionClass не определен");
        }
        return exceptionClass.isInstance(ex);
    }

    @Override
    public final CommonErrorMessageDto getErrorMessage(final Exception ex, ErrorMessageMapper errorMessageMapper) {
        beforeBuildErrorMessage(ex);

        CommonErrorMessageDto errorMessage;

        if (ex instanceof AbstractRuntimeException) {
            errorMessage = errorMessageMapper.map((AbstractRuntimeException) ex);
        } else {
            errorMessage = errorMessageMapper.defaultMap(ex);
        }

        afterBuildErrorMessage(errorMessage);

        return errorMessage;
    }

    @Override
    public void beforeBuildErrorMessage(Exception ex) {
    }

    @Override
    public void afterBuildErrorMessage(CommonErrorMessageDto errorMessage) {
    }
}
