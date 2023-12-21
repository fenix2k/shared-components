package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception.JpaDataAccessException;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.mapper.CommonMapper;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.CommonEntity;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service.api.BaseJpaService;

import java.util.Optional;

/**
 * Базовый CRUD сервис с mapstruct для конвертации в DTO.
 *
 * @param <E> тип сущности
 * @param <D> тип dto сущности
 * @param <ID> тип идентификатора сущности
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class AbstractBaseJpaService<E extends CommonEntity, D, ID>
        extends AbstractCoreJpaService<E, ID> implements BaseJpaService<E, D, ID> {

    @Override
    public abstract CommonMapper<E, D> getMapper();

    @Override
    public Page<D> findAll(Pageable pageable) {
        Page<E> page = findAllEntity(pageable);
        return page.map(getMapper()::toDto);
    }

    @Override
    public Optional<D> findById(ID id) {
        Optional<E> entity = findEntityById(id);
        return entity.map(e -> getMapper().toDto(e));
    }

    @Override
    public D getById(ID id) {
        E entity = getEntityById(id);
        return getMapper().toDto(entity);
    }

    @Override
    @Transactional
    public D create(D dto) {
        E entity = getMapper().toEntity(dto);
        beforeSave(dto);
        beforeCreate(dto);
        D saved = getMapper().toDto(create(entity));
        afterCreate(dto, saved);
        afterSave(dto, saved);
        return saved;
    }

    @Override
    @Transactional
    public D update(ID id, D dto) {
        if (id == null) {
            throw new JpaDataAccessException(ExceptionCodes.INVALID_ARGUMENT, MSG_ENTITY_ATTR_ID_NOT_PRESENT);
        }
        E entity = getMapper().toEntity(dto);
        beforeSave(dto);
        beforeUpdate(dto);
        D saved = getMapper().toDto(update(id, entity));
        afterUpdate(dto, saved);
        afterSave(dto, saved);
        return saved;
    }

    /**
     * Выполнить что-то перед сохранением сущности (операции CREATE|UPDATE).
     *
     * @param dtoToSave дто сущности перед сохранением/обновлением {@link D}
     */
    public void beforeSave(D dtoToSave) {
    }

    /**
     * Выполнить что-то после сохранения сущности (операции CREATE|UPDATE).
     *
     * @param dtoBeforeSave дто сущности перед сохранением/обновлением {@link D}
     * @param dtoAfterSave дто сущности после сохранения/обновлением {@link D}
     */
    public void afterSave(D dtoBeforeSave, D dtoAfterSave) {
    }

    /**
     * Выполнить что-то перед созданием сущности (операция CREATE).
     *
     * @param dtoToCreate dto сущности перед созданием {@link D}
     */
    public void beforeCreate(D dtoToCreate) {
    }

    /**
     * Выполнить что-то после создания сущности (операция CREATE).
     *
     * @param dtoToCreate dto сущности перед созданием {@link D}
     * @param dtoAfterCreate dto сущности после создания {@link D}
     */
    public void afterCreate(D dtoToCreate, D dtoAfterCreate) {
    }

    /**
     * Выполнить что-то перед обновлением сущности (операция UPDATE).
     *
     * @param dtoBeforeUpdate dto сущности перед обновлением {@link D}
     */
    public void beforeUpdate(D dtoBeforeUpdate) {
    }

    /**
     * Выполнить что-то после обновления сущности (операция UPDATE).
     *
     * @param dtoBeforeUpdate dto сущности перед обновлением {@link D}
     * @param dtoAfterUpdate dto сущности после обновления {@link D}
     */
    public void afterUpdate(D dtoBeforeUpdate, D dtoAfterUpdate) {
    }
}
