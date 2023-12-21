package ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers;

import ru.atc.mvd.gismu.shared2.exceptionhandler.core.exceptions.AbstractRuntimeException;

/**
 * Обработчик по-умолчанию.
 */
public class DefaultExceptionHandler extends AbstractExceptionHandler
        implements ExceptionHandler {

    @Override
    public Class<? extends AbstractRuntimeException> exceptionClass() {
        return AbstractRuntimeException.class;
    }
}
