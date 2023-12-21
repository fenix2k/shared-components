package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api;

/**
 * Интерфейс преобразователя типов.
 */
@SuppressWarnings("unused")
public interface TypeCaster {

    /**
     * Получить тип.
     *
     * @return {@link Class}
     */
    Class<?> getType();

    /**
     * Преобразовывает value к типу type.
     *
     * @param value {@link Object}
     * @return {@link Object}
     */
    Object cast(Object value);
}
