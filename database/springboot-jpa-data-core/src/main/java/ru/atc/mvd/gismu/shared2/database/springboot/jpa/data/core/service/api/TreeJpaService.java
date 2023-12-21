package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.service.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.TreeEntityAttributes;

import java.util.Optional;

/**
 * Интерфейс CRUD сервиса с mapstruct для конвертации в DTO c возможностью построения иерархии.
 *
 * @param <E> тип сущности
 * @param <D> тип dto сущности
 * @param <ID> тип идентификатора сущности
 */
@SuppressWarnings("unused")
public interface TreeJpaService<E extends TreeEntityAttributes<ID>, D, ID>
        extends ExtendedJpaService<E, D, ID> {

    /**
     * Получить родителя E.
     *
     * @param entity {@link E}
     * @return {@link Optional}<{@link E}>
     */
    Optional<E> getParent(E entity);

    /**
     * Получить потомков по ID постранично.
     *
     * @param id id {@link ID}
     * @param pageable {@link Pageable}
     * @return {@link Page}<{@link E}>
     */
    Page<E> getChildren(ID id, Pageable pageable);

    /**
     * Получить признак имеет ли объект потомков по id.
     *
     * @param id id {@link ID}
     * @return boolean
     */
    boolean hasChildren(ID id);

    /**
     * Получить кол-во потомков по id.
     *
     * @param id id {@link ID}
     * @return boolean
     */
    int getChildrenCount(ID id);

    /**
     * Получить E верхнего уровня постранично.
     *
     * @param pageable {@link Pageable}
     * @return {@link E}
     */
    Page<E> getRoots(Pageable pageable);

    /**
     * Является ли объект с parentId родителем child.
     *
     * @param child {@link E}
     * @param parentId id {@link ID}
     * @return boolean
     */
    boolean isChild(E child, ID parentId);

    /**
     * Является ли объект с parentId прямым родителем child.
     * @param child {@link E}
     * @param parentId id {@link ID}
     * @return boolean
     */
    boolean isDirectChild(E child, ID parentId);
}
