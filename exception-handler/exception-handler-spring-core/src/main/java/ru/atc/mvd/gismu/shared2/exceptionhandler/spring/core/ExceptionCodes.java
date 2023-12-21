package ru.atc.mvd.gismu.shared2.exceptionhandler.spring.core;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.CommonExceptionCodes;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.ExceptionCode;

/**
 * Типы ошибок.
 */
@Getter
@SuppressWarnings("unused")
public class ExceptionCodes extends CommonExceptionCodes {

    // Ошибки связанные с Spring Context
    public static final ExceptionCode BEAN_IS_MISSING =
            new CommonExceptionCodes("0500", "SPRING", "Необходимый бин не найден");

    public ExceptionCodes(String code, String type, String description) {
        super(code, type, description);
    }

    public ExceptionCodes(String code, String type, String description, HttpStatus httpStatus) {
        super(code, type, description, httpStatus.value());
    }
}