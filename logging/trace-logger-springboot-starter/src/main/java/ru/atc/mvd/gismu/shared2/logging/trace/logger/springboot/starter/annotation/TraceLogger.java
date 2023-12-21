package ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для трассировки выполнения методов.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface TraceLogger {

    /**
     * Флаг, выводить ли имя класс в сокращенном виде.
     *
     * @return boolean
     */
    boolean showShortClassName() default true;

    /**
     * Флаг, выводить ли возвращаемое значение.
     *
     * @return boolean
     */
    boolean showReturnValue() default true;

    /**
     * Массив индексов параметров, которые необходимо исключить из логирования.
     *
     * @return int array
     */
    int[] excludeArgIndex() default -10;
}
