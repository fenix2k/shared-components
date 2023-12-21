package ru.atc.mvd.gismu.shared2.communication.openfeign.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация указывается на то что Interceptor общий для все клиентов.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FeignGlobalInterceptor {
}
