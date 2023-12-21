package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.wrapper;

import lombok.Getter;

/**
 * Обёртка для сохранения состояния сущности до и после операций.
 *
 * @param <E> тип сущности
 */
@Getter
public class EntityDiffWrapper<E> {

    private final E before;
    private final E after;

    public EntityDiffWrapper(E before, E after) {
        this.before = before;
        this.after = after;
    }
}
