package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service.api;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.SimpleEntityAttributes;

/**
 * Интерфейс базового CRUD сервиса с mapstruct для конвертации в DTO.
 *
 * @param <E> тип сущности
 * @param <D> тип dto сущности
 * @param <ID> тип идентификатора сущности
 */
@SuppressWarnings("unused")
public interface SimpleJpaService<E extends SimpleEntityAttributes, D, ID>
        extends BaseJpaService<E, D, ID> {
}
