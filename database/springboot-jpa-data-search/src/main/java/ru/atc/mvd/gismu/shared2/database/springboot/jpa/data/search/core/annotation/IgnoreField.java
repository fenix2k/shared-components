package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Помечает поле которое не должно участвовать в формировании строки поиска.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@SuppressWarnings("unused")
public @interface IgnoreField {
}
