package ru.atc.mvd.gismu.shared2.exceptionhandler.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.config.properties.CommonExceptionHandlerProperties;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.dto.CommonErrorMessageDto;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.exceptions.CommonRuntimeException;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers.DefaultExceptionHandler;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers.ExceptionHandler;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.handlers.ExceptionHandlerManager;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.mapper.DefaultErrorMessageMapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

class MainTest {

    private static ExceptionHandlerManager exceptionHandlerManager;

    @BeforeAll
    static void initExceptionHandler() {
        List<ExceptionHandler> exceptionHandlers = Collections.singletonList(new DefaultExceptionHandler());

        CommonExceptionHandlerProperties properties = new CommonExceptionHandlerProperties();
        properties.setDefaultExceptionCode(CommonExceptionCodes.COMMON);
        properties.setShowFullMessage(true);
        properties.setExcludeFullMessageFilterByCode(new HashSet<>(Collections.singletonList("C0050")));
        properties.setExcludeFullMessageFilterByType(new HashSet<>(Collections.singletonList("COMMON")));

        exceptionHandlerManager = new ExceptionHandlerManager(exceptionHandlers, new DefaultErrorMessageMapper(properties));
    }

    @Test
    void testWithCommonExceptionCode() {
        CommonErrorMessageDto targetErrorMessage = new CommonErrorMessageDto(
                "Common error",
                null,
                "Какая-то общая ошибка",
                null,
                "COMMON",
                "C0000",
                500,
                null
        );

        doTestCommonRuntimeException(CommonExceptionCodes.COMMON, targetErrorMessage);
    }

    @Test
    void testWithArgumentExceptionCode() {
        CommonErrorMessageDto targetErrorMessage = new CommonErrorMessageDto(
                "Entity not found",
                null,
                "Сущность не найдена",
                null,
                "IO",
                "C0050",
                400,
                null
        );

        doTestCommonRuntimeException(CommonExceptionCodes.ENTITY_NOT_FOUND, targetErrorMessage);
    }

    @Test
    void testWithFileAccessErrorCode() {
        CommonErrorMessageDto targetErrorMessage = new CommonErrorMessageDto(
                "File access error",
                "File access error. Cause = Runtime Exception cause",
                "Ошибка доступа к файлу",
                null,
                "FS",
                "C0102",
                500,
                null
        );

        doTestCommonRuntimeException(CommonExceptionCodes.FILE_ACCESS_ERROR, targetErrorMessage);
    }

    @Test
    void testWithJavaException() {
        CommonErrorMessageDto targetErrorMessage = new CommonErrorMessageDto(
                "Argument is not valid",
                null,
                "Общая ошибка",
                null,
                "COMMON",
                "C0000",
                500,
                null
        );

        doTestRuntimeException(targetErrorMessage);
    }

    void doTestCommonRuntimeException(ExceptionCode exceptionCode, CommonErrorMessageDto targetErrorMessage) {
        Exception causeException;
        try {
            throw new RuntimeException("Runtime Exception cause");
        } catch (Exception ex) {
            causeException = ex;
        }
        try {
            throw new CommonRuntimeException(exceptionCode,
                    targetErrorMessage.getMessage(), targetErrorMessage.getLocalizedMessage(), causeException);
        } catch (Exception ex) {
            CommonErrorMessageDto errorMessage = exceptionHandlerManager.getErrorMessage(ex);

            Assertions.assertNotNull(errorMessage);
            Assertions.assertEquals(targetErrorMessage.getMessage(), errorMessage.getMessage());
            Assertions.assertEquals(targetErrorMessage.getFullMessage(), errorMessage.getFullMessage());
            Assertions.assertEquals(targetErrorMessage.getLocalizedMessage(), errorMessage.getLocalizedMessage());
            Assertions.assertEquals(targetErrorMessage.getDetails(), errorMessage.getDetails());
            Assertions.assertEquals(targetErrorMessage.getType(), errorMessage.getType());
            Assertions.assertEquals(targetErrorMessage.getCode(), errorMessage.getCode());
            Assertions.assertEquals(targetErrorMessage.getStatus(), errorMessage.getStatus());
            Assertions.assertNotNull(errorMessage.getTimestamp());
        }
    }

    void doTestRuntimeException(CommonErrorMessageDto targetErrorMessage) {
        Exception causeException;
        try {
            throw new NullPointerException("NullPointer exception cause");
        } catch (Exception ex) {
            causeException = ex;
        }
        try {
            throw new RuntimeException(targetErrorMessage.getMessage(), causeException);
        } catch (Exception ex) {
            CommonErrorMessageDto errorMessage = exceptionHandlerManager.getErrorMessage(ex);

            Assertions.assertNotNull(errorMessage);
            Assertions.assertEquals(targetErrorMessage.getMessage(), errorMessage.getMessage());
            Assertions.assertEquals(targetErrorMessage.getFullMessage(), errorMessage.getFullMessage());
            Assertions.assertEquals(targetErrorMessage.getLocalizedMessage(), errorMessage.getLocalizedMessage());
            Assertions.assertEquals(targetErrorMessage.getDetails(), errorMessage.getDetails());
            Assertions.assertEquals(targetErrorMessage.getType(), errorMessage.getType());
            Assertions.assertEquals(targetErrorMessage.getCode(), errorMessage.getCode());
            Assertions.assertEquals(targetErrorMessage.getStatus(), errorMessage.getStatus());
            Assertions.assertNotNull(errorMessage.getTimestamp());
        }
    }
}