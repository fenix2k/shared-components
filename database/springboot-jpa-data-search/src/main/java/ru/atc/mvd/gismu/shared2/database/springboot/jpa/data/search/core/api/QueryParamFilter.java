package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api;

import java.util.List;

/**
 * Интерфейс фильтра параметров запроса.
 */
@SuppressWarnings("unused")
public interface QueryParamFilter {

    /**
     * Возвращает список полей фильтра.
     *
     * @return {@link List}<{@link String}>
     */
    List<String> getFilter();

    /**
     * Возвращает резельтат фильтрации.
     *
     * @param fieldName {@link String}
     * @return boolean
     */
    boolean doFilter(String fieldName);
}
