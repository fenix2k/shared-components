package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception.JpaDataAccessException;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.TreeEntityAttributes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.repository.TreeJpaRepository;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service.api.TreeJpaService;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Расширенный CRUD сервис с mapstruct для конвертации в DTO c возможностью построения иерархии.
 *
 * @param <E> тип сущности
 * @param <D> тип dto сущности
 * @param <ID> тип идентификатора сущности
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class AbstractTreeJpaService<E extends TreeEntityAttributes<ID>, D, ID>
        extends AbstractExtendedBaseJpaService<E, D, ID> implements TreeJpaService<E, D, ID> {

    private static final String MSG_CREATE_ERR_CYCLE_DEP_NOT_PERMITTED =
            "Ошибка создания: циклические зависимости не допустимы";
    private static final String MSG_CREATE_ERR_PARENT_ENTITY_NOT_FOUND =
            "Ошибка создания: родитель с указанными атрибутами (parentId=[%s]) не найден";
    private static final String MSG_UPDATE_ERR_PARENT_CHANGE_NOT_PERMITTED =
            "Ошибка обновления: не допускается изменение ссылки на родительский объект";
    private static final String MSG_DELETE_ERR_ENTITY_HAS_CHILDREN =
            "Ошибка удаления: запись имеет потомков";

    /**
     * Реализация репозитория.
     *
     * @return {@link TreeJpaRepository}
     */
    public abstract TreeJpaRepository<E, ID> getRepository();

    /**
     * Получить корневой ID родителя.
     *
     * @return {@link ID}
     */
    public abstract ID getRootParentId();

    @Override
    public void beforeSave(E entityToSave, E entityFromDb) {
        // Проверяем имеет ли объект потомков
        if (entityFromDb != null) {
            entityToSave.setHasChildren(getRepository().existsByParentIdAndActionIndIn(entityToSave.getId(),
                    ACTION_IND_NO_DELETED));
        } else {
            entityToSave.setHasChildren(false);
        }

        Optional<E> parent = getParent(entityToSave);
        if (parent.isPresent()) {
            // Установка атрибута path
            String prevPath = parent.get().getRootPath() != null &&
                    !parent.get().getRootPath().isEmpty() ? parent.get().getRootPath() + "." : "";
            entityToSave.setRootPath(prevPath + parent.get().getId());
            parent.get().setHasChildren(true);

            // Проверка на циклические зависимости
            Set<String> parentIds = Arrays.stream(entityToSave.getRootPath().split("\\."))
                    .collect(Collectors.toSet());
            if (entityToSave.getId() != null && parentIds.contains(entityToSave.getId().toString())) {
                throw new JpaDataAccessException(ExceptionCodes.INVALID_OPERATION, MSG_CREATE_ERR_CYCLE_DEP_NOT_PERMITTED);
            }
        } else {
            entityToSave.setParentId(getRootParentId());
            entityToSave.setRootPath("");
        }
        super.beforeSave(entityToSave, entityFromDb);
    }

    @Override
    public void beforeCreate(E entityToSave) {
        ID parentId = entityToSave.getParentId();
        if (parentId != null && !parentId.toString().isEmpty()) {
            findEntityById(parentId).orElseThrow(
                    () -> new JpaDataAccessException(ExceptionCodes.ENTITY_NOT_FOUND,
                            String.format(MSG_CREATE_ERR_PARENT_ENTITY_NOT_FOUND, parentId)));
        }
        super.beforeCreate(entityToSave);
    }

    @Override
    public void beforeUpdate(E entityToSave, E entityFromDb) {
        if (!Objects.equals(entityToSave.getParentId(), entityFromDb.getParentId())) {
            throw new JpaDataAccessException(ExceptionCodes.INVALID_OPERATION, MSG_UPDATE_ERR_PARENT_CHANGE_NOT_PERMITTED);
        }
        super.beforeUpdate(entityToSave, entityFromDb);
    }

    @Override
    public void beforeDelete(E entity) {
        if (getRepository().existsByParentIdAndActionIndIn(entity.getId(), ACTION_IND_NO_DELETED)) {
            throw new JpaDataAccessException(ExceptionCodes.INVALID_OPERATION, MSG_DELETE_ERR_ENTITY_HAS_CHILDREN);
        }
        Optional<E> parent = getParent(entity);
        if (parent.isPresent()) {
            int childCount = getRepository().countByParentIdAndActionIndIn(parent.get().getId(), ACTION_IND_NO_DELETED);
            if (childCount <= 1) {
                parent.get().setHasChildren(false);
            }
        }
        super.beforeDelete(entity);
    }

    @Override
    public Optional<E> getParent(E entity) {
        if (entity == null) {
            return Optional.empty();
        }
        return findEntityById(entity.getParentId());
    }

    @Override
    public Page<E> getChildren(ID id, Pageable pageable) {
        return getRepository()
                .findAllByParentIdAndActionIndIn(id, ACTION_IND_NO_DELETED, buildPageable(pageable));
    }

    @Override
    public boolean hasChildren(ID id) {
        return getRepository().existsByParentIdAndActionIndIn(id, ACTION_IND_NO_DELETED);
    }

    @Override
    public int getChildrenCount(ID id) {
        return getRepository().countByParentIdAndActionIndIn(id, ACTION_IND_NO_DELETED);
    }

    @Override
    public Page<E> getRoots(Pageable pageable) {
        return getRepository()
                .findAllByParentIdAndActionIndIn(getRootParentId(), ACTION_IND_NO_DELETED, buildPageable(pageable));
    }

    @Override
    public boolean isChild(E child, ID parentId) {
        String rootPath = child.getRootPath();
        if (rootPath.isEmpty()) {
            return false;
        }
        return Arrays.asList(rootPath.split("\\."))
                .contains(parentId.toString());
    }

    @Override
    public boolean isDirectChild(E child, ID parentId) {
        return Objects.equals(child.getParentId(), parentId);
    }
}
