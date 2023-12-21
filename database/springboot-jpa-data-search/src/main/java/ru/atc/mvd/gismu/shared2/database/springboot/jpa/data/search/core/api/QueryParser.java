package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api;

import java.util.Deque;

/**
 * Фильтр параметров запроса.
 *
 * @param <D> объект запроса
 */
@SuppressWarnings("unused")
public interface QueryParser<D> {

    /**
     * Парсит поисковый запрос и возвращает в виде очереди.
     *
     * @param searchParam поисковый запрос {@link D}
     * @return очередь {@link Deque}
     */
    Deque<?> parse(D searchParam);
}
