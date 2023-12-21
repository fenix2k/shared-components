package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service;

import lombok.extern.slf4j.Slf4j;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.mapper.CommonMapper;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.SimpleEntityAttributes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.repository.SimpleJpaRepository;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service.api.SimpleJpaService;

import java.time.LocalDateTime;

/**
 * Базовый CRUD сервис с mapstruct для конвертации в DTO.
 *
 * @param <E> тип сущности
 * @param <D> тип dto сущности
 * @param <ID> тип идентификатора сущности
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class AbstractSimpleBaseJpaService<E extends SimpleEntityAttributes, D, ID>
        extends AbstractBaseJpaService<E, D, ID> implements SimpleJpaService<E, D, ID> {

    @Override
    public abstract CommonMapper<E, D> getMapper();

    @Override
    public abstract SimpleJpaRepository<E, ID> getRepository();

    @Override
    protected void protectedBeforeCreate(E entity) {
        entity.setCreatedDttm(LocalDateTime.now());
        entity.setModifyDttm(LocalDateTime.now());
    }

    @Override
    protected void protectedBeforeUpdate(E entity) {
        entity.setModifyDttm(LocalDateTime.now());
    }
}
