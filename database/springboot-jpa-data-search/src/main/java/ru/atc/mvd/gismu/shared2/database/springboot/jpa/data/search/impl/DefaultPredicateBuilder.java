package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.PredicateBuilder;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.CompareOperations;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.JpaDataSearchException;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Реализация интерфейса построения предиката.
 */
public class DefaultPredicateBuilder implements PredicateBuilder {

    /**
     * Возвращает предикат в соответствии с типом операции.
     *
     * @param builder {@link CriteriaBuilder}
     * @param operation {@link CompareOperations}
     * @param from {@link From}
     * @param key {@link String}
     * @param value {@link Object}
     * @return {@link Predicate}
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Predicate build(CriteriaBuilder builder, From<?, ?> from,
                           CompareOperations operation,
                           String key,
                           Object value) {
        if (from == null) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "Параметр [from] не может быть null");
        }
        if (key == null || key.isEmpty()) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "Параметр [key] не может быть null или пустым");
        }

        switch (operation) {
            case EQUALS : return builder.equal(from.get(key), value);
            case EQUALS_IGNORE_CASE : return builder.equal(builder.lower(from.get(key)), value.toString().toLowerCase());
            case NOT_EQUALS: return builder.notEqual(from.get(key), value);
            case NOT_EQUALS_IGNORE_CASE: return builder.notEqual(builder.lower(from.get(key)), value.toString().toLowerCase());
            case GREATER : return builder.greaterThan(from.get(key), (Comparable) value);
            case GREATER_OR_EQUALS : return builder.greaterThanOrEqualTo(from.get(key), (Comparable) value);
            case LESS : return builder.lessThan(from.get(key), (Comparable) value);
            case LESS_OR_EQUALS : return builder.lessThanOrEqualTo(from.get(key), (Comparable) value);
            case STARTS : return builder.like(from.get(key), value + "%");
            case STARTS_IGNORE_CASE : return builder.like(builder.lower(from.get(key)), value.toString().toLowerCase() + "%");
            case ENDS : return builder.like(from.get(key), "%" + value);
            case ENDS_IGNORE_CASE : return builder.like(builder.lower(from.get(key)), "%" + value.toString().toLowerCase());
            case CONTAINS : return builder.like(from.get(key), "%" + value + "%");
            case CONTAINS_IGNORE_CASE : return builder.like(builder.lower(from.get(key)), "%" + value.toString().toLowerCase() + "%");
            case IN : return buildIn(builder, from, key, value);
            case BETWEEN : return buildBetween(builder, from, key, value);
            default : return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Predicate buildIn(CriteriaBuilder builder, From<?, ?> from,
                              String key, Object value) {
        if (Arrays.asList(value.getClass().getInterfaces()).contains(List.class)) {
            List<Object> list = new ArrayList<>((List<Object>) value);

            if (list.size() != 2) {
                throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT,
                        String.format("Операция [IN] должна иметь хотя бы 1 аргумент. Поле: [%s]", key));
            }

            return builder.in(from.get(key)).value(value);
        }

        throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "неверный тип данных для оператора IN");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Predicate buildBetween(CriteriaBuilder builder, From<?, ?> from,
                                   String key, Object value) {
        if (Arrays.asList(value.getClass().getInterfaces()).contains(List.class)) {
            List<Object> list = new ArrayList<>((List<Object>) value);

            if (list.size() != 2) {
                throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT,
                        String.format("Операция [BETWEEN] должна иметь 2 аргумента. Поле: [%s]", key));
            }

            Predicate onStart = builder.greaterThanOrEqualTo(from.get(key), (Comparable) list.get(0));
            Predicate onEnd = builder.lessThanOrEqualTo(from.get(key), (Comparable) list.get(1));

            return builder.and(onStart, onEnd);
        }

        throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "неверный тип данных для оператора BETWEEN");
    }
}
