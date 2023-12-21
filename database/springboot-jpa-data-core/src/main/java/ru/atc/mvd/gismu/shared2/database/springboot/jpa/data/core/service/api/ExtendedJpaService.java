package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.ExtendedEntityAttributes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.enums.ActionIndex;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Интерфейс расширенного CRUD сервиса с mapstruct для конвертации в DTO.
 *
 * @param <E> тип сущности
 * @param <D> тип dto сущности
 * @param <ID> тип идентификатора сущности
 */
@SuppressWarnings("unused")
public interface ExtendedJpaService<E extends ExtendedEntityAttributes, D, ID>
        extends SimpleJpaService<E, D, ID> {

    /**
     * Получить список сущностей с определённым индикатором постранично.
     *
     * @param actionInds список индикаторов изменения записи {@link Set}<{@link ActionIndex}>
     * @param pageable параметры пагинации {@link Pageable}
     * @return страница со списком сущностей {@link Page}<{@link E}>
     */
    Page<E> findAllEntity(Set<ActionIndex> actionInds, Pageable pageable);

    /**
     * Получить список активных сущностей постранично.
     *
     * @param pageable параметры пагинации {@link Pageable}
     * @return страница со списком сущностей {@link Page}<{@link E}>
     */
    Page<E> findAllEntity(Pageable pageable);

    /**
     * Получить список сущностей по идентификаторам.
     *
     * @param ids идентификаторы {@link Set}<{@link ID}>
     * @param actionInds список индикаторов изменения записи {@link Set}<{@link ActionIndex}>
     * @return список сущностей {@link List}<{@link E}>
     */
    List<E> findAllEntity(Set<ID> ids, Set<ActionIndex> actionInds);

    /**
     * Получить список dto сущностей с определённым индикатором постранично.
     *
     * @param actionInds список индикаторов изменения записи {@link Set}<{@link ActionIndex}>
     * @param pageable параметры пагинации {@link Pageable}
     * @return список dto сущностей постранично {@link Page}<{@link D}>
     */
    Page<D> findAll(Set<ActionIndex> actionInds, Pageable pageable);

    /**
     * Получить список dto активных сущностей постранично.
     *
     * @param pageable параметры пагинации {@link Pageable}
     * @return список dto сущностей постранично {@link Page}<{@link D}>
     */
    Page<D> findAll(Pageable pageable);

    /**
     * Получить сущность по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @param withDeleted с удалёнными записями
     * @return сущность {@link Optional}<{@link E}>
     */
    Optional<E> findEntityById(ID id, boolean withDeleted);

    /**
     * Получить активную сущность по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @return сущность {@link Optional}<{@link E}>
     */
    Optional<E> findEntityById(ID id);

    /**
     * Получить сущность по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @param withDeleted с удалёнными записями
     * @return сущность {@link E}
     * @throws EntityNotFoundException Entity Not Found Exception
     */
    E getEntityById(ID id, boolean withDeleted) throws EntityNotFoundException;

    /**
     * Получить активную сущность по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @return сущность {@link E}
     * @throws EntityNotFoundException Entity Not Found Exception
     */
    E getEntityById(ID id) throws EntityNotFoundException;

    /**
     * Получить dto сущности по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @param withDeleted с удалёнными записями
     * @return dto сущности {@link Optional}<{@link D}>
     */
    Optional<D> findById(ID id, boolean withDeleted);

    /**
     * Получить dto активной сущности по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @return dto сущности {@link Optional}<{@link D}>
     */
    Optional<D> findById(ID id);

    /**
     * Получить dto сущности по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @param withDeleted с удалёнными записями
     * @return dto сущности {@link Optional}<{@link D}>
     */
    D getById(ID id, boolean withDeleted);

    /**
     * Получить dto активной сущности по идентификатору.
     *
     * @param id идентификатор {@link ID}
     * @return dto сущности {@link Optional}<{@link D}>
     */
    D getById(ID id) throws EntityNotFoundException;

    /**
     * Удалить сущность (пометить как удалённую) (операция DELETE).
     *
     * @param id {@link ID}
     */
    void delete(ID id);

    /**
     * Удалить сущность (пометить как удалённую) (операция DELETE).
     *
     * @param entity {@link E}
     */
    void delete(E entity);
}
