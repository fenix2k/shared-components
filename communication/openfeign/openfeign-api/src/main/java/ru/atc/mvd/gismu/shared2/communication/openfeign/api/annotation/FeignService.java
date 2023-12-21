package ru.atc.mvd.gismu.shared2.communication.openfeign.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация указывает что класс является сервисом feign с соответствующим кодом.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignService {

    /** Код сервиса. */
    String code();

    /** Url сервиса. */
    String url() default "";

    /** Имя сервиса. */
    String name() default "";
}
