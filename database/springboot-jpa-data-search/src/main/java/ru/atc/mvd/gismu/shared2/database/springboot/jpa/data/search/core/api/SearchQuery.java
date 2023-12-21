package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.CompareOperations;

import java.util.Map;
import java.util.Optional;

/**
 * Интерфейс поискового запроса.
 */
public interface SearchQuery {

    String OPERATION_MAP_PROPERTY_NAME = "searchOperationMap";

    /**
     * Возвращает мапу[атрибут, тип операции].
     *
     * @return {@link Map}<{@link String}, {@link CompareOperations}>
     */
    Map<String, CompareOperations> getSearchOperationMap();

    /**
     * Добавляет мапу[атрибут, тип операции].
     *
     * @param searchOperationMap {@link Map}<{@link String}, {@link CompareOperations}>
     */
    void setSearchOperationMap(Map<String, CompareOperations> searchOperationMap);

    /**
     * Валидация атрибутов поиска.
     *
     * @return мапа с ошибками
     */
    Optional<Map<String, String>> validate();

    /**
     * Определить маппинг операций к полям.
     *
     * @return мапа с маппингом операций к полям
     */
    Optional<Map<String, CompareOperations>> operationMap();
}
