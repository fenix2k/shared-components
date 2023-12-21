package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Коды ошибок.
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum ExceptionCodes {

    INVALID_ARGUMENT("Неверный аргумент"),
    SEARCH_ERROR("Общая ошибка поиска"),
    TYPE_CAST("Ошибка преобразования типа"),
    INVALID_SEARCH_QUERY("Неверные параметры поиска");

    private final String description;
}
