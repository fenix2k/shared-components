package ru.atc.mvd.gismu.shared2.exceptionhandler.core.mapper;

import ru.atc.mvd.gismu.shared2.exceptionhandler.core.dto.CommonErrorMessageDto;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.exceptions.AbstractRuntimeException;

/**
 * Интерфейс маппера для ErrorMessage.
 */
@SuppressWarnings("unused")
public interface ErrorMessageMapper {

    /**
     * Получить дто с описанием ошибки из AbstractRuntimeException.
     *
     * @param ex {@link AbstractRuntimeException}
     * @return {@link CommonErrorMessageDto}
     */
    CommonErrorMessageDto map(AbstractRuntimeException ex);

    /**
     * Получить дто с описанием ошибки из Exception.
     *
     * @param ex {@link Exception}
     * @return {@link CommonErrorMessageDto}
     */
    CommonErrorMessageDto defaultMap(Exception ex);
}
