package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception.JpaDataAccessException;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.CommonEntity;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.wrapper.EntityDiffWrapper;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.repository.CommonJpaRepository;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service.api.CoreJpaService;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.utils.ServiceUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Общий CRUD сервис.
 *
 * @param <E> тип сущности
 * @param <ID> тип идентификатора сущности
 */
@Slf4j
@SuppressWarnings("unused")
public abstract class AbstractCoreJpaService<E extends CommonEntity, ID>
        implements CoreJpaService<E, ID> {

    protected static final String MSG_ENTITY_NOT_FOUND_BY_ID = "Запись c id=%s не найдена";
    protected static final String MSG_ENTITY_NOT_PRESENT = "Параметр метода [entity] не определён";
    protected static final String MSG_ENTITY_ATTR_ID_NOT_PRESENT = "Атрибут сущности [id] не определён";
    protected static final String MSG_DATA_ACCESS_ERROR = "Ошибка доступа к хранилищу: %s";

    protected static final Pageable DEFAULT_PAGEABLE = PageRequest.of(0, 50);
    protected static final int MAX_PAGE_SIZE = 100;
    protected static final int BATCH_SIZE = 1000;

    @PersistenceContext
    protected EntityManager entityManager;

    /**
     * Реализация репозиторий сущности.
     *
     * @return {@link CommonJpaRepository}
     */
    public abstract CommonJpaRepository<E, ID> getRepository();

    /**
     * Возвращает Pageable по-умолчанию.
     *
     * @return параметры пагинации {@link Pageable}
     */
    public Pageable getPageable() {
        return DEFAULT_PAGEABLE;
    }

    /**
     * Формирует итоговый Pageable.
     *
     * @param pageable параметры пагинации {@link Pageable}
     * @return {@link Pageable}
     */
    public Pageable buildPageable(Pageable pageable) {
        if (pageable == null) {
            return getPageable();
        }
        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            return PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE, pageable.getSort());
        }
        return pageable;
    }

    @Override
    public Page<E> findAllEntity(Pageable pageable) {
        return getRepository().findAll(buildPageable(pageable));
    }

    @Override
    public Optional<E> findEntityById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public E getEntityById(ID id) {
        return findEntityById(id).orElseThrow(
                () -> new JpaDataAccessException(ExceptionCodes.ENTITY_NOT_FOUND,
                        String.format(MSG_ENTITY_NOT_FOUND_BY_ID, id)));
    }

    @Override
    @Transactional
    public E create(E entity) {
        // Операции перед созданием
        beforeSave(entity, null);
        beforeCreate(entity);

        // Копируем объект перед созданием, чтобы запомнить его состояние
        E entityBeforeCreate;
        try {
            entityBeforeCreate = ServiceUtils.copyObject(entity);
        } catch (Exception ex) {
            throw new JpaDataAccessException(ExceptionCodes.CONVERSION_ERROR,
                    String.format("Не удалось создать копию объекта сохранения: %s", ex.getLocalizedMessage()));
        }
        // Для установки атрибутов сущности (расширение)
        protectedBeforeCreate(entity);
        // Создание новой сущности
        E entityAfterCreate = getRepository().save(entity);

        // Операции после создания
        afterCreate(entityBeforeCreate, entityAfterCreate);
        afterSave(entityBeforeCreate, entityAfterCreate);

        return entityAfterCreate;
    }

    @Override
    @Transactional
    public EntityDiffWrapper<E> updateAndGetDiff(ID id, E entity) {
        if (id == null) {
            throw new JpaDataAccessException(ExceptionCodes.INVALID_ARGUMENT, MSG_ENTITY_ATTR_ID_NOT_PRESENT);
        }

        // Убираем обёртку HibernateProxy если она есть
        E entityBeforeUpdate = ServiceUtils.hibernateEntityUnproxy(entity);
        // Получаем текущее значение объекта из БД
        E entityFromDb = getEntityById(id);
        // Сохраняем текущее состояние объекта
        E entityFromDbBeforeUpdate;
        try {
            entityFromDbBeforeUpdate = ServiceUtils.copyObject(entityFromDb);
        } catch (Exception ex) {
            throw new JpaDataAccessException(ExceptionCodes.CONVERSION_ERROR,
                    String.format("Не удалось создать копию объекта сохранения: %s", ex.getLocalizedMessage()));
        }

        // Операции перед обновлением
        beforeSave(entityBeforeUpdate, entityFromDb);
        beforeUpdate(entityBeforeUpdate, entityFromDb);

        // Для установки атрибутов сущности (расширение)
        protectedBeforeUpdate(entityBeforeUpdate);
        // Слияние
        try {
            ServiceUtils.mergeNotNullObjectsFields(entityFromDb, entityBeforeUpdate);
        } catch (Exception ex) {
            throw new JpaDataAccessException(ExceptionCodes.CONVERSION_ERROR,
                    String.format("Не удалось объединить объект сохранения с объектом из БД: %s", ex.getLocalizedMessage()));
        }
        // Обновление
        E entityAfterUpdate = getRepository().save(entityFromDb);

        // Операции после обновления
        afterUpdate(entityFromDbBeforeUpdate, entityAfterUpdate);
        afterSave(entityFromDbBeforeUpdate, entityAfterUpdate);

        return new EntityDiffWrapper<>(entityFromDbBeforeUpdate, entityAfterUpdate);
    }

    @Override
    @Transactional
    public E update(ID id, E entity) {
        return updateAndGetDiff(id, entity).getAfter();
    }

    /**
     * Создаёт или обновляет сущности пакетно.
     * Позволяет существенно ускорить массовое создание/обновление сущностей.
     * Метод сервиса, который вызывает этот метод, необходимо пометить аннотацией @Transactional.
     * Пакетные операции работают только если у сущности @GeneratedValue(strategy = GenerationType.SEQUENCE).
     *
     * @param entities список сущностей {@link List}<{@link E}>
     */
    private void saveBatch(List<E> entities, boolean allowUpdate) {
        for (int i = 0; i < entities.size(); i++) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                entityManager.flush();
                entityManager.clear();
            }
            E entity = entities.get(i);
            if (entity == null) {
                continue;
            }

            if (allowUpdate) {
                // Для установки атрибутов сущности (расширение)
                protectedBeforeUpdate(entity);
                entityManager.persist(entity);
            } else {
                // Для установки атрибутов сущности (расширение)
                protectedBeforeCreate(entity);
                entityManager.merge(entity);
            }
        }
    }

    @Override
    @Transactional
    public void createBatch(List<E> entities) {
        saveBatch(entities, false);
    }

    @Override
    @Transactional
    public void updateBatch(List<E> entities) {
        saveBatch(entities, true);
    }

    @Override
    public void deletePermanent(ID id) {
        beforeDeletePermanent(id);
        getRepository().deleteById(id);
        afterDeletePermanent(id);
    }

    @Override
    public void deletePermanent(E entity) {
        if (entity == null) {
            throw new JpaDataAccessException(ExceptionCodes.INVALID_ARGUMENT, MSG_ENTITY_NOT_PRESENT);
        }
        beforeDeletePermanent(entity);
        protectedBeforeDeletePermanent(entity);
        getRepository().delete(entity);
        afterDeletePermanent(entity);
    }

    /**
     * Метод используется для установки атрибутов сущности перед сохранением.
     * Использовать только при расширении абстрактными классами.
     *
     * @param entity сущность перед сохранением {@link E}
     */
    protected void protectedBeforeCreate(E entity) {
    }

    /**
     * Метод используется для установки атрибутов сущности перед обновлением.
     * Использовать только при расширении абстрактными классами.
     *
     * @param entity сущность перед обновлением {@link E}
     */
    protected void protectedBeforeUpdate(E entity) {
    }

    /**
     * Метод используется для установки атрибутов сущности перед полным удалением.
     * Использовать только при расширении абстрактными классами.
     *
     * @param entity сущность перед полным удалением {@link E}
     */
    protected void protectedBeforeDeletePermanent(E entity) {
    }

    /**
     * Выполнить что-то перед сохранением сущности (операции CREATE|UPDATE).
     *
     * @param entityBeforeSave сущность перед сохранением/обновлением {@link E}
     * @param entityFromDb сущность, которая храниться в БД {@link E}
     */
    public void beforeSave(E entityBeforeSave, E entityFromDb) {
    }

    /**
     * Выполнить что-то после сохранения сущности (операции CREATE|UPDATE).
     *
     * @param entityBeforeSave сущность перед сохранением/обновлением {@link E}
     * @param entityAfterSave сущность после сохранения/обновлением {@link E}
     */
    public void afterSave(E entityBeforeSave, E entityAfterSave) {
    }

    /**
     * Выполнить что-то перед созданием сущности (операция CREATE).
     *
     * @param entityToCreate сущность перед созданием {@link E}
     */
    public void beforeCreate(E entityToCreate) {
    }

    /**
     * Выполнить что-то после создания сущности (операция CREATE).
     *
     * @param entityToCreate сущность перед созданием {@link E}
     * @param entityAfterCreate сущность после создания {@link E}
     */
    public void afterCreate(E entityToCreate, E entityAfterCreate) {
    }

    /**
     * Выполнить что-то перед обновлением сущности (операция UPDATE).
     *
     * @param entityBeforeUpdate сущность перед обновлением {@link E}
     * @param entityFromDb сущность, которая храниться в БД {@link E}
     */
    public void beforeUpdate(E entityBeforeUpdate, E entityFromDb) {
    }

    /**
     * Выполнить что-то после обновления сущности (операция UPDATE).
     *
     * @param entityBeforeUpdate сущность перед обновлением {@link E}
     * @param entityAfterUpdate сущность после обновления {@link E}
     */
    public void afterUpdate(E entityBeforeUpdate, E entityAfterUpdate) {
    }

    /**
     * Выполнить что-то перед полным удалением сущности (операция DELETE_PERMANENT).
     *
     * @param id идентификатор {@link ID}
     */
    public void beforeDeletePermanent(ID id) {
    }

    /**
     * Выполнить что-то перед полным удалением сущности (операция DELETE_PERMANENT).
     *
     * @param entityToDelete сущность {@link E}
     */
    public void beforeDeletePermanent(E entityToDelete) {
    }

    /**
     * Выполнить что-то после полного удаления сущности (операция DELETE_PERMANENT).
     *
     * @param id идентификатор удалённой сущности {@link ID}
     */
    public void afterDeletePermanent(ID id) {
    }

    /**
     * Выполнить что-то после полного удаления сущности (операция DELETE_PERMANENT).
     *
     * @param entityBeforeDelete сущность перед удалением {@link E}
     */
    public void afterDeletePermanent(E entityBeforeDelete) {
    }
}
