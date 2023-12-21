package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception.JpaDataAccessException;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.mapper.CommonMapper;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.ExtendedEntityAttributes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.enums.ActionIndex;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.repository.ExtendedJpaRepository;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service.api.ExtendedJpaService;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Расширенный CRUD сервис с mapstruct для конвертации в DTO.
 *
 * @param <E> тип сущности
 * @param <D> тип dto сущности
 * @param <ID> тип идентификатора сущности
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class AbstractExtendedBaseJpaService<E extends ExtendedEntityAttributes, D, ID>
        extends AbstractSimpleBaseJpaService<E, D, ID> implements ExtendedJpaService<E, D, ID> {

    public static final Set<ActionIndex> ACTION_IND_ALL = Arrays.stream(ActionIndex.values()).collect(Collectors.toSet());
    public static final Set<ActionIndex> ACTION_IND_NO_DELETED = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(ActionIndex.I, ActionIndex.U)));
    public static final Set<ActionIndex> ACTION_IND_DELETED = Collections.unmodifiableSet(
            new HashSet<>(Collections.singletonList(ActionIndex.D)));

    protected static final String MSG_PERMANENT_DELETE_NOT_PERMIT = "Операция полного удаления не допустима";

    @Override
    public abstract CommonMapper<E, D> getMapper();

    @Override
    public abstract ExtendedJpaRepository<E, ID> getRepository();

    @Override
    protected void protectedBeforeCreate(E entity) {
        super.protectedBeforeCreate(entity);
        entity.setActionInd(ActionIndex.I);
    }

    @Override
    protected void protectedBeforeUpdate(E entity) {
        super.protectedBeforeCreate(entity);
        entity.setActionInd(ActionIndex.U);
    }

    @Override
    public Page<E> findAllEntity(Set<ActionIndex> actionInds, Pageable pageable) {
        return getRepository().findAllByActionIndIn(actionInds, pageable);
    }

    @Override
    public Page<E> findAllEntity(Pageable pageable) {
        return findAllEntity(ACTION_IND_NO_DELETED, buildPageable(pageable));
    }

    @Override
    public List<E> findAllEntity(Set<ID> ids, Set<ActionIndex> actionInds) {
        return getRepository().findAllByIdInAndActionIndIn(ids, actionInds);
    }

    @Override
    public Page<D> findAll(Set<ActionIndex> actionInds, Pageable pageable) {
        Page<E> page = findAllEntity(actionInds, pageable);
        return page.map(getMapper()::toDto);
    }

    @Override
    public Page<D> findAll(Pageable pageable) {
        Page<E> page = findAllEntity(pageable);
        return page.map(getMapper()::toDto);
    }

    @Override
    public Optional<E> findEntityById(ID id, boolean withDeleted) {
        return getRepository().findById(id)
                .filter(v -> withDeleted || v.getActionInd() != ActionIndex.D);
    }

    @Override
    public Optional<E> findEntityById(ID id) {
        return findEntityById(id, false);
    }

    @Override
    public E getEntityById(ID id, boolean withDeleted) throws EntityNotFoundException {
        return findEntityById(id, withDeleted).orElseThrow(
                () -> new JpaDataAccessException(ExceptionCodes.ENTITY_NOT_FOUND,
                        String.format(MSG_ENTITY_NOT_FOUND_BY_ID, id)));
    }

    @Override
    public E getEntityById(ID id) throws EntityNotFoundException {
        return getEntityById(id, false);
    }

    @Override
    public Optional<D> findById(ID id, boolean withDeleted) {
        Optional<E> entity = findEntityById(id, withDeleted);
        return entity.map(e -> getMapper().toDto(e));
    }

    @Override
    public Optional<D> findById(ID id) {
        return findById(id, false);
    }

    @Override
    public D getById(ID id, boolean withDeleted) {
        E entity = getEntityById(id, withDeleted);
        return getMapper().toDto(entity);
    }

    @Override
    public D getById(ID id) {
        E entity = getEntityById(id);
        return getMapper().toDto(entity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        beforeDelete(id);
        E entity = getEntityById(id, true);
        delete(entity);
    }

    @Override
    public void delete(E entity) {
        if (entity == null) {
            throw new JpaDataAccessException(ExceptionCodes.INVALID_ARGUMENT, MSG_ENTITY_NOT_PRESENT);
        }
        beforeDelete(entity);
        protectedBeforeDelete(entity);
        getRepository().save(entity);
        afterDelete(entity);
    }

    @Override
    protected void protectedBeforeDeletePermanent(E entity) {
        throw new JpaDataAccessException(ExceptionCodes.INVALID_OPERATION, MSG_PERMANENT_DELETE_NOT_PERMIT);
    }

    /**
     * Метод используется для установки атрибутов сущности перед удалением.
     * Использовать только при расширении абстрактными классами.
     *
     * @param entity сущность перед удалением {@link E}
     */
    protected void protectedBeforeDelete(E entity) {
        entity.setActionInd(ActionIndex.D);
    }

    /**
     * Выполнить что-то перед удалением сущности (операция DELETE).
     *
     * @param id идентификатор удалённой сущности {@link ID}
     */
    public void beforeDelete(ID id) {
    }

    /**
     * Выполнить что-то перед удалением сущности (операция DELETE).
     *
     * @param entityToDelete сущность {@link E}
     */
    public void beforeDelete(E entityToDelete) {
    }

    /**
     * Выполнить что-то после удаления сущности (операция DELETE).
     *
     * @param id идентификатор удалённой сущности {@link ID}
     */
    public void afterDelete(ID id) {
    }

    /**
     * Выполнить что-то после удаления сущности (операция DELETE).
     *
     * @param entityBeforeDelete сущность {@link E}
     */
    public void afterDelete(E entityBeforeDelete) {
    }
}
