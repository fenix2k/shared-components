package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Операторы группировки для соединения поисковых параметров.
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum SearchFieldTypes {

    SIMPLE("Простое поле"),
    ENTITY_OBJECT("Вложенная сущность"),
    COMPOSITE_OR_OBJECT("Составной объект с условием ИЛИ");

    /** Токен операции. */
    private final String description;
}
