package ru.atc.mvd.gismu.shared2.exceptionhandler.springboot2.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.dto.CommonErrorMessageDto;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers.ExceptionHandlerManager;
import ru.atc.mvd.gismu.shared2.exceptionhandler.spring.core.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.exceptionhandler.spring.core.dto.ErrorMessageDto;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Формирование HTTP ответов в случае ошибок.
 * Перехватывает ЛЮБОЕ исключение.
 */
@Slf4j
public class ExceptionHandlerControllerAdviser extends ResponseEntityExceptionHandler {
    
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

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.MISSING_REQUEST_PARAM_VALUE.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError ->
                        String.format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.joining(", "));
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setMessage(message);
        errorMessageDto.setType(ExceptionCodes.NOT_VALID_PARAM_VALUE.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.HTTP_MESSAGE_NOT_READABLE.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.HTTP_MESSAGE_NOT_WRITABLE.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatus status,
                                                                         WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.HTTP_METHOD_NOT_SUPPORTED.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.HTTP_MEDIA_TYPE_NOT_SUPPORTED.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers,
                                                                      HttpStatus status,
                                                                      WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex,
                                                               HttpHeaders headers,
                                                               HttpStatus status,
                                                               WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.MISSING_PATH_VARIABLE.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.CONVERSION_NOT_SUPPORTED.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
                                                        HttpHeaders headers,
                                                        HttpStatus status,
                                                        WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.TYPE_MISMATCH.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatus status,
                                                                          WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.SERVLET_REQUEST_BINDING.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers,
                                                                     HttpStatus status,
                                                                     WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.MISSING_SERVLET_REQUEST_PART.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex,
                                                         HttpHeaders headers,
                                                         HttpStatus status,
                                                         WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.BIND_EXCEPTION.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.NO_HANDLER_FOUND.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex,
                                                                        HttpHeaders headers,
                                                                        HttpStatus status,
                                                                        WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.ASYNC_REQUEST_TIMEOUT.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        errorMessageDto.setType(ExceptionCodes.EXCEPTION_INTERNAL.getDescription());
        return new ResponseEntity<>(errorMessageDto, headers, errorMessageDto.getStatus());
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

        ErrorMessageDto errorMessageDto = new ErrorMessageDto(commonErrorMessage, error, getRequestPath(request).orElse(""));
        log.info("Error response: {}", errorMessageDto);
        return errorMessageDto;
    }

    private Optional<String> getRequestPath(WebRequest request) {
        if (request == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(((ServletWebRequest)request).getRequest().getRequestURI());
    }
}
