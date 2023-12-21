package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.CommonEntity;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.wrapper.EntityDiffWrapper;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс общего CRUD сервиса.
 *
 * @param <E> тип сущности
 * @param <ID> тип идентификатора сущности
 */
@SuppressWarnings("unused")
public interface CoreJpaService<E extends CommonEntity, ID> {

    /**
     * Получить список сущностей постранично.
     *
     * @param pageable параметры пагинации {@link Pageable}
     * @return страница со списком сущностей {@link Page}<{@link E}>
     */
    Page<E> findAllEntity(Pageable pageable);

    /**
     * Получить сущность по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @return сущность {@link Optional}<{@link E}>
     */
    Optional<E> findEntityById(ID id);

    /**
     * Получить сущность по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @return сущность {@link E}
     */
    E getEntityById(ID id);

    /**
     * Сохранить новую сущность (операция CREATE).
     *
     * @param entity сущность {@link E}
     * @return сохраненная сущность {@link E}
     */
    @Transactional
    E create(E entity);

    /**
     * Обновить сущность и получить состояние сущности до и после (операция UPDATE).
     *
     * @param id идентификатор {@link ID}
     * @param entity сущность {@link E}
     * @return сущность до и после обновления {@link EntityDiffWrapper}<{@link E}>
     */
    @Transactional
    EntityDiffWrapper<E> updateAndGetDiff(ID id, E entity);

    /**
     * Обновить сущность (операция UPDATE).
     *
     * @param id идентификатор {@link ID}
     * @param entity сущность {@link E}
     * @return обновленная сущность {@link E}
     */
    @Transactional
    E update(ID id, E entity);

    /**
     * Создать сущности пакетно.
     * Позволяет существенно ускорить массовое создание сущностей.
     * Метод сервиса, который вызывает этот метод, необходимо пометить аннотацией @Transactional.
     * Пакетные операции работают только если у сущности @GeneratedValue(strategy = GenerationType.SEQUENCE).
     *
     * @param entities список сущностей {@link List}<{@link E}>
     */
    @Transactional
    void createBatch(List<E> entities);

    /**
     * Обновляет сущности пакетно.
     * Позволяет существенно ускорить массовое обновление сущностей.
     * Метод сервиса, который вызывает этот метод, необходимо пометить аннотацией @Transactional.
     * Пакетные операции работают только если у сущности @GeneratedValue(strategy = GenerationType.SEQUENCE).
     *
     * @param entities список сущностей {@link List}<{@link E}>
     */
    @Transactional
    void updateBatch(List<E> entities);

    /**
     * Удалить сущность (навсегда) (операция DELETE_PERMANENT).
     *
     * @param id идентификатор {@link ID}
     */
    void deletePermanent(ID id);

    /**
     * Удалить сущность (навсегда) (операция DELETE_PERMANENT).
     *
     * @param entity сущность {@link E}
     */
    void deletePermanent(E entity);
}
