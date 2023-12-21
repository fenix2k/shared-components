package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.CompareOperations;

/**
 * Specification Search Criteria.
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class SearchCriteria {

    /** Ключ (название атрибута сущности). */
    private final String key;

    /** Операция (:,<,> и т.д.). */
    private final CompareOperations operation;

    /** Значение. */
    private final Object value;
}
