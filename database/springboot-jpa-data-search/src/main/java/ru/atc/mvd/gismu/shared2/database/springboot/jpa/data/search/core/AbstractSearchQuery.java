package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.SearchQuery;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.CompareOperations;

import java.util.Map;
import java.util.Optional;

/**
 * Указывает что поисковые параметры поддерживают указание типа операции сравнения.
 */
@SuppressWarnings("unused")
public abstract class AbstractSearchQuery implements SearchQuery {

    private Map<String, CompareOperations> searchOperationMap;

    @Override
    public Map<String, CompareOperations> getSearchOperationMap() {
        return searchOperationMap;
    }

    @Override
    public void setSearchOperationMap(Map<String, CompareOperations> searchOperationMap) {
        this.searchOperationMap = searchOperationMap;
    }

    @Override
    public Optional<Map<String, String>> validate() {
        return Optional.empty();
    }

    @Override
    public Optional<Map<String, CompareOperations>> operationMap() {
        return Optional.empty();
    }
}
