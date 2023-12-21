package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api;

/**
 * Интерфейс провайдера преобразователей типа.
 */
@SuppressWarnings("unused")
public interface TypeCasterProvider {

    /**
     * Добавить typeCaster.
     *
     * @param typeCaster {@link TypeCaster}
     */
    void addTypeCaster(TypeCaster typeCaster);

    /**
     * Преобразовать значение value к типу fieldType.
     *
     * @param fieldType тип поля {@link Class}
     * @param value значение {@link Class}
     * @return {@link Object}
     */
    Object cast(Class<?> fieldType, Object value);
}
