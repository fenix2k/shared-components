package ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers;

import lombok.extern.slf4j.Slf4j;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.dto.CommonErrorMessageDto;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.mapper.ErrorMessageMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Менеджер обработчиков ошибок.
 */
@Slf4j
@SuppressWarnings("unused")
public class ExceptionHandlerManager {

    private final Map<String, ExceptionHandler> errorExceptionHandlers;
    private final ErrorMessageMapper errorMessageMapper;

    public ExceptionHandlerManager(List<ExceptionHandler> exceptionHandlers,
                                   ErrorMessageMapper errorMessageMapper) {
        super();
        this.errorMessageMapper = errorMessageMapper;
        this.errorExceptionHandlers = exceptionHandlers.stream()
                .collect(Collectors.toMap(h -> h.getClass().getCanonicalName(), h -> h));
        exceptionHandlers.forEach(h -> log.debug("Register ErrorExceptionHandler: {}",
                h.getClass().getCanonicalName()));
    }

    /**
     * Получить дто с описанием ошибки исходя из типа ошибки.
     */
    public CommonErrorMessageDto getErrorMessage(Exception ex) {
        return errorExceptionHandlers.values().stream()
                .filter(h -> h.accept(ex))
                .findFirst()
                .orElse(new DefaultExceptionHandler())
                .getErrorMessage(ex, errorMessageMapper);
    }

    /**
     * Получить имена ErrorExceptionHandler.
     *
     * @return {@link List}<{@link String}>
     */
    public List<String> getErrorExceptionHandlerNames() {
        return new ArrayList<>(errorExceptionHandlers.keySet());
    }
}
