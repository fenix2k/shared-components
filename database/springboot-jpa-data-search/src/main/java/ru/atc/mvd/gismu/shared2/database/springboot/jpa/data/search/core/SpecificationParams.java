package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core;

import lombok.Getter;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.PredicateBuilder;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.TypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.TypeCasterProvider;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.JpaDataSearchException;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.DefaultPredicateBuilder;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.DefaultTypeCasterProvider;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types.BooleanTypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types.BooleanWrapperTypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types.DateTypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types.LocalDateTimeTypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types.LocalDateTypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types.OffsetDateTimeTypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types.UUIDTypeCaster;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Set;

/**
 * Параметры поиска по спецификации.
 *
 * @param <E> entity class
 */
@Getter
public class SpecificationParams<E> {

    /** Класс сущности */
    private final Class<E> entityClass;

    /** Билдер предикатов. */
    private final PredicateBuilder predicateBuilder;

    /** Преобразователь типов. */
    private final TypeCasterProvider typeCasterProvider;

    /** Список полей сущностей, которые будут включены в select запрос результата поиска. */
    private final Set<String> entityGraph;

    /**
     * Конструктор.
     *
     * @param entityClass класс сущности {@link Class}
     * @param predicateBuilder билдер предикатов {@link PredicateBuilder}
     * @param typeCasterProvider провайдер преобразователей типов {@link TypeCasterProvider}
     * @param entityGraph граф зависимостей {@link Set}<{@link String}>
     */
    public SpecificationParams(Class<E> entityClass,
                               PredicateBuilder predicateBuilder,
                               TypeCasterProvider typeCasterProvider,
                               Set<String> entityGraph) {
        if (entityClass == null) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "Параметр [entityClass] не может быть null");
        }
        if (!checkEntityClass(entityClass)) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT,
                    String.format("Класс [%s] не является сущностью JPA (javax.persistence.Entity)", entityClass));
        }
        this.entityClass = entityClass;
        this.predicateBuilder = predicateBuilder != null ?
                predicateBuilder : defaultPredicateBuilder();
        this.typeCasterProvider = typeCasterProvider != null ?
                typeCasterProvider : defaultTypeCasterProvider();
        this.entityGraph = entityGraph;

    }

    /**
     * Конструктор.
     *
     * @param entityClass класс сущности {@link Class}
     */
    public SpecificationParams(Class<E> entityClass) {
        this(entityClass, null, null, null);
    }

    /**
     * Проверяет, является ли класс сущностью JPA.
     *
     * @param entityClass класс сущности {@link Class}
     * @return boolean
     */
    private boolean checkEntityClass(Class<E> entityClass) {
        return entityClass.isAnnotationPresent(Entity.class);
    }

    private PredicateBuilder defaultPredicateBuilder() {
        return new DefaultPredicateBuilder();
    }

    private TypeCasterProvider defaultTypeCasterProvider() {
        ArrayList<TypeCaster> typeCasters = new ArrayList<>();

        typeCasters.add(new BooleanTypeCaster());
        typeCasters.add(new BooleanWrapperTypeCaster());
        typeCasters.add(new DateTypeCaster());
        typeCasters.add(new LocalDateTypeCaster());
        typeCasters.add(new LocalDateTimeTypeCaster());
        typeCasters.add(new OffsetDateTimeTypeCaster());
        typeCasters.add(new UUIDTypeCaster());

        return new DefaultTypeCasterProvider(typeCasters);
    }
}
