package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Коды ошибок.
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionCodes {

    INVALID_ARGUMENT("Неверный аргумент"),
    ENTITY_NOT_FOUND("Запись не найдена"),
    DUPLICATE_FOUND("Найден дубликат записи"),
    INVALID_OPERATION("Неверная операция"),
    CONVERSION_ERROR("Ошибка преобразования");

    private final String description;
}
