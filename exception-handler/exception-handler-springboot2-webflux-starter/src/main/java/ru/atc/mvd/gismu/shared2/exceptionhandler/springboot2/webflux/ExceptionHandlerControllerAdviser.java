package ru.atc.mvd.gismu.shared2.exceptionhandler.springboot2.webflux;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.dto.CommonErrorMessageDto;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers.ExceptionHandlerManager;
import ru.atc.mvd.gismu.shared2.exceptionhandler.spring.core.dto.ErrorMessageDto;

/**
 * Формирование HTTP ответов в случае ошибок.
 * Перехватывает ЛЮБОЕ исключение.
 */
@Slf4j
public class ExceptionHandlerControllerAdviser {
    
    private final ExceptionHandlerManager exceptionHandlerManager;

    public ExceptionHandlerControllerAdviser(ExceptionHandlerManager exceptionHandlerManager) {
        super();
        this.exceptionHandlerManager = exceptionHandlerManager;
        log.info("ErrorExceptionHandlers: {}", exceptionHandlerManager.getErrorExceptionHandlerNames());
    }

    /**
     * Общий обработчик исключений.
     *
     * @param request {@link WebRequest}
     * @param ex {@link Exception}
     * @return {@link ResponseEntity}<{@link ErrorMessageDto}>
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDto> handleException(WebRequest request, Exception ex) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        return new ResponseEntity<>(errorMessageDto, new HttpHeaders(), errorMessageDto.getStatus());
    }

    private ErrorMessageDto buildErrorMessage(WebRequest request, Exception ex) {
        log.info("Handle exception: type=[{}], message=[{}]", ex.getClass(), ex.getLocalizedMessage());
        log.debug("Exception stacktrace: ", ex);
        CommonErrorMessageDto commonErrorMessage = exceptionHandlerManager.getErrorMessage(ex);

        String error = null;
        HttpStatus httpStatus = HttpStatus.resolve(commonErrorMessage.getStatus());
        if (httpStatus != null) {
            error = httpStatus.getReasonPhrase();
        }

        ErrorMessageDto errorMessageDto = new ErrorMessageDto(commonErrorMessage, error, request.getContextPath());
        log.info("Error response: {}", errorMessageDto);
        return errorMessageDto;
    }
}
