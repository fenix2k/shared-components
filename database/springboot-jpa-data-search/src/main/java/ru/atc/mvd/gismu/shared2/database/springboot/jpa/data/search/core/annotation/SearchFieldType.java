package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.annotation;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.SearchFieldTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Указывает тип поля (как его обрабатывать).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
public @interface SearchFieldType {

    /** Обобщенный тип поля. */
    SearchFieldTypes type();
}
