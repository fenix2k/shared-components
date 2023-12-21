package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.CompareOperations;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;

/**
 * Функциональный интерфейс построения предиката.
 */
@FunctionalInterface
@SuppressWarnings("unused")
public interface PredicateBuilder {

    /**
     * Строитель предикатов.
     *
     * @param builder {@link CriteriaBuilder}
     * @param from {@link From}
     * @param operation {@link CompareOperations}
     * @param key {@link String}
     * @param value {@link Object}
     * @return {@link Predicate}
     */
    Predicate build(CriteriaBuilder builder,
                    From<?, ?> from,
                    CompareOperations operation,
                    String key,
                    Object value);
}
