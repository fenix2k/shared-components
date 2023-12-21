package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Добавляет поля для выстраивания иерархии.
 *
 * @param <ID> тип идентификатора сущности
 */
@MappedSuperclass
@Getter
@Setter
@SuppressWarnings("unused")
public abstract class TreeEntityAttributes<ID> extends ExtendedEntityAttributes {

    /**
     * Ид родителя.
     */
    @Column(name = "parent_id", insertable = false, updatable = false)
    protected ID parentId;

    /**
     * Путь от корня.
     */
    @Column(name = "root_path")
    protected String rootPath;

    /**
     * Признак наличия потомков.
     */
    @Column(name = "has_children")
    protected Boolean hasChildren;

    /**
     * Получить идентификатор сущности.
     *
     * @return {@link ID}
     */
    public abstract ID getId();

    /**
     * Получить идентификатор родительской сущности.
     *
     * @return {@link ID}
     */
    public abstract ID getParentId();
}
