package ru.atc.mvd.gismu.shared2.exceptionhandler.core.mapper;

import ru.atc.mvd.gismu.shared2.exceptionhandler.core.ExceptionCode;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.config.properties.CommonExceptionHandlerProperties;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.dto.CommonErrorMessageDto;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.exceptions.AbstractRuntimeException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

/**
 * Базовый маппер для ErrorMessageMapper.
 */
@SuppressWarnings("unused")
public class DefaultErrorMessageMapper implements ErrorMessageMapper {

    private final CommonExceptionHandlerProperties properties;

    public DefaultErrorMessageMapper(final CommonExceptionHandlerProperties properties) {
        this.properties = properties;
    }

    @Override
    public CommonErrorMessageDto map(AbstractRuntimeException ex) {
        String fullMessage = null;
        int httpStatusCode = ex.getHttpStatusCode();

        ExceptionCode exCode = ex.getCode() != null ? ex.getCode() : properties.getDefaultExceptionCode();

        String type = this.requireNonNullElse(exCode.getType(), "");
        String code = this.requireNonNullElse(exCode.getModule(), "") +
                this.requireNonNullElse(exCode.getCode(), "");

        if (httpStatusCode < 0) {
            httpStatusCode = exCode.getHttpStatusCode();
        }

        return new CommonErrorMessageDto(
                ex.getMessage(),
                getFullMessage(ex, exCode),
                ex.getLocalizedMessage(),
                ex.getDetails(),
                type,
                code,
                httpStatusCode,
                String.valueOf(getTimestamp())
        );
    }

    @Override
    public CommonErrorMessageDto defaultMap(Exception ex) {
        String fullMessage = null;
        ExceptionCode exCode = properties.getDefaultExceptionCode();

        String type = this.requireNonNullElse(exCode.getType(), "");
        String code = this.requireNonNullElse(exCode.getModule(), "") +
                this.requireNonNullElse(exCode.getCode(), "");

        return new CommonErrorMessageDto(
                ex.getMessage(),
                getFullMessage(ex, exCode),
                exCode.getDescription(),
                null,
                type,
                code,
                exCode.getHttpStatusCode(),
                String.valueOf(getTimestamp())
        );
    }

    private String getFullMessage(Exception ex, ExceptionCode exceptionCode) {
        if (properties.isShowFullMessage()) {
            if (properties.getExcludeFullMessageFilterByType() != null &&
                    properties.getExcludeFullMessageFilterByType().contains(exceptionCode.getType())) {
                return null;
            }

            String exCode = this.requireNonNullElse(exceptionCode.getModule(), "") + exceptionCode.getCode();
            if (properties.getExcludeFullMessageFilterByCode() != null &&
                    properties.getExcludeFullMessageFilterByCode().contains(exCode)) {
                return null;
            }

            StringBuilder fullMessage = new StringBuilder(ex.getMessage());
            if (ex.getCause() != null) {
                fullMessage.append(". Cause = ");
                fullMessage.append(ex.getCause().getMessage());
            }
            return fullMessage.toString();
        }
        return null;
    }

    private long getTimestamp() {
        ZoneId zoneId = ZoneId.systemDefault();
        return LocalDateTime.now().atZone(zoneId).toInstant().toEpochMilli();
    }

    //копипаста из https://github.com/AdoptOpenJDK/openjdk-jdk11/blob/master/src/java.base/share/classes/java/util/Objects.java#L300C5-L302C6
    private <T> T requireNonNullElse(T obj, T defaultObj) {
        return (obj != null) ? obj : Objects.requireNonNull(defaultObj, "defaultObj");
    }
}
