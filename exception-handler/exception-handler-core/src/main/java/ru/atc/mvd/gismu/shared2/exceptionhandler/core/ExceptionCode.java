package ru.atc.mvd.gismu.shared2.exceptionhandler.core;

/**
 * Интерфейс для кодов ошибок.
 */
@SuppressWarnings("unused")
public interface ExceptionCode {

    /** Получить код модуля. */
    String getModule();

    /** Получить код ошибки. */
    String getCode();

    /** Получить тип ошибки. */
    String getType();

    /** Получить описание ошибки. */
    String getDescription();

    /** Получить код http статуса. */
    int getHttpStatusCode();
}
