package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.queryparser;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.AbstractSearchQuery;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.SearchCriteria;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.annotation.IgnoreField;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.annotation.SearchFieldType;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.annotation.VirtualField;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.QueryParser;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.SearchQuery;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.CompareOperations;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.LogicalOperations;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums.SearchFieldTypes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.JpaDataSearchException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Формирует поисковый запрос на основе Dto.
 * Поисковые атрибуты соединяются логическим оператором AND.
 *
 * @param <D> тип дто поиска
 */
@SuppressWarnings("unused")
public class DtoQueryParser<D> implements QueryParser<D> {

    /** Операция сравнения по-умолчанию. */
    private static final CompareOperations DEFAULT_COMPARISON_OPERATION = CompareOperations.EQUALS;

    /** Логическая операция по-умолчанию. */
    private static final LogicalOperations DEFAULT_LOGICAL_OPERATION = LogicalOperations.AND;

    private static final HashSet<Class<?>> ALLOWED_GENERIC_TYPES = new HashSet<>();

    static {
        ALLOWED_GENERIC_TYPES.add(String.class);
        ALLOWED_GENERIC_TYPES.add(Long.class);
        ALLOWED_GENERIC_TYPES.add(Integer.class);
        ALLOWED_GENERIC_TYPES.add(Date.class);
        ALLOWED_GENERIC_TYPES.add(LocalDate.class);
        ALLOWED_GENERIC_TYPES.add(LocalDateTime.class);
        ALLOWED_GENERIC_TYPES.add(OffsetDateTime.class);
        ALLOWED_GENERIC_TYPES.add(UUID.class);
    }

    /**
     * Настройки операции для атрибутов поиска.
     * Вид: имя атрибута, операция.
     */
    private Map<String, CompareOperations> operationMap = new HashMap<>();

    public DtoQueryParser() {
    }

    public DtoQueryParser(Map<String, CompareOperations> operationMap) {
        if (operationMap != null) {
            this.operationMap = operationMap;
        }
    }

    /**
     * Парсит поисковый запрос и возвращает в виде очереди.
     *
     * @param searchQuery поисковый запрос {@link D}
     * @return очередь {@link Deque}
     */
    @Override
    public Deque<?> parse(D searchQuery) {
        if (isInheritOf(searchQuery.getClass(), AbstractSearchQuery.class)) {
            Map<String, CompareOperations> searchOperationMap = ((AbstractSearchQuery) searchQuery).getSearchOperationMap();
            if (searchOperationMap != null) {
                mergeOperationMap(searchOperationMap);
            }
        }

        try {
            return buildDeque(null, searchQuery, DEFAULT_LOGICAL_OPERATION);
        } catch (Exception ex) {
            throw new JpaDataSearchException(ExceptionCodes.SEARCH_ERROR, ex.getMessage());
        }
    }

    /**
     * Проверить унаследован ли указанный класс от родителя.
     *
     * @param clazz исходный класс {@link Class}
     * @param parent родительский класс {@link Class}
     * @return boolean
     */
    private boolean isInheritOf(Class<?> clazz, Class<?> parent) {
        if (clazz.equals(AbstractSearchQuery.class)) {
            return true;
        } else {
            return isInheritOf(clazz.getSuperclass(), parent);
        }
    }

    /**
     * Производит слияние мапы операций operationMap с searchOperationMap.
     * В случае совпадения ключей приоритет имеет operationMap.
     *
     * @param searchOperationMap {@link Map}<{@link String}, {@link CompareOperations}>
     */
    private void mergeOperationMap(Map<String, CompareOperations> searchOperationMap) {
        if (searchOperationMap == null) {
            return;
        }
        for (Map.Entry<String, CompareOperations> entry : searchOperationMap.entrySet()) {
            if (!this.operationMap.containsKey(entry.getKey())) {
                this.operationMap.put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * Получить все поля класса включая родительские.
     *
     * @param clazz {@link Class}
     * @return {@link List}<{@link Field}>
     */
    private List<Field> getAllFields(Class<?> clazz) {
        if (clazz == null || clazz.equals(AbstractSearchQuery.class)) {
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
        List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toList());
        result.addAll(filteredFields);
        return result;
    }

    /**
     * Возвращает стек поиска исходя из параметров searchQuery.
     *
     * @param parentFieldName имя атрибута родителя {@link String}
     * @param searchQuery параметры поиска {@link Object}
     * @param logicalOperation {@link LogicalOperations}
     * @return строка поиска {@link String}
     */
    private Deque<?> buildDeque(String parentFieldName, Object searchQuery, LogicalOperations logicalOperation)
            throws Exception {
        Deque<Object> resultStack = new LinkedList<>();
        int newCommandCount = 0;

        LogicalOperations logOp = logicalOperation;

        Object currentStackObject;

        for (Field field : getAllFields(searchQuery.getClass())) {
            // Фильтр по аннотации @IgnoreField
            if (field.isAnnotationPresent(IgnoreField.class)) {
                continue;
            }

            String fieldName = field.getName();

            // Фильтр по имени поля
            if (field.getName().equalsIgnoreCase(SearchQuery.OPERATION_MAP_PROPERTY_NAME)) {
                continue;
            }

            field.setAccessible(true);
            Object fieldValue = field.get(searchQuery);

            // Фильтр по null значению поля
            if (fieldValue == null) {
                continue;
            }

            // Получение типа поля
            SearchFieldTypes searchFieldType = SearchFieldTypes.SIMPLE;
            if (field.isAnnotationPresent(SearchFieldType.class)) {
                searchFieldType = field.getAnnotation(SearchFieldType.class).type();
            }

            // Получение операции сравнения на поля
            CompareOperations compareOperation = getComparisonOperator(fieldName);

            // Псевдоним поля
            if (field.isAnnotationPresent(VirtualField.class)) {
                String virtualFieldValue = field.getAnnotation(VirtualField.class).fieldName();
                fieldName = parentFieldName == null ? virtualFieldValue : parentFieldName + "." + virtualFieldValue;
            } else {
                fieldName = parentFieldName == null ? fieldName : parentFieldName + "." + fieldName;
            }

            if (SearchFieldTypes.ENTITY_OBJECT.equals(searchFieldType)) {
                currentStackObject = buildFieldTypeEntityObject(field, fieldName, fieldValue, logOp);
            } else if (SearchFieldTypes.COMPOSITE_OR_OBJECT.equals(searchFieldType)) {
                currentStackObject = buildDeque(null, fieldValue, LogicalOperations.OR);
            } else {
                currentStackObject = buildFieldTypeSimple(field, fieldName, fieldValue, compareOperation);
            }

            if (currentStackObject == null) {
                continue;
            }

            resultStack.push(currentStackObject);
            newCommandCount++;

            if (newCommandCount >= logOp.getNumOfArgs()) {
                resultStack.push(logOp);
                newCommandCount = 1;
            }
        }

        return resultStack;
    }

    /**
     * Возвращает оператор сравнения для указанного атрибута исходя из мапы operationMap.
     * Если в operationMap значение отсутствует, то возвращается DEFAULT_COMPARISON_OPERATION.
     *
     * @param fieldName имя атрибута {@link String}
     * @return операция сравнения {@link CompareOperations}
     */
    private CompareOperations getComparisonOperator(String fieldName) {
        if (operationMap == null || operationMap.isEmpty()) {
            return DEFAULT_COMPARISON_OPERATION;
        }
        return operationMap.getOrDefault(fieldName, DEFAULT_COMPARISON_OPERATION);
    }

    /**
     * Формирует объект для ипа поля SearchFieldTypes.ENTITY_OBJECT.
     */
    private Object buildFieldTypeEntityObject(Field field, String fieldName,
                                              Object fieldValue, LogicalOperations logicalOperation)
            throws Exception {
        Object result = null;
        if (Collection.class.isAssignableFrom(field.getType())) {
            Optional<?> obj = ((Collection<?>) fieldValue).stream().findFirst();
            if (obj.isPresent()) {
                result = buildDeque(fieldName, obj.get(), LogicalOperations.AND);
            }
        } else {
            result = buildDeque(fieldName, fieldValue, logicalOperation);
        }
        return result;
    }

    /**
     * Формирует объект для ипа поля SearchFieldTypes.SIMPLE.
     */
    @SuppressWarnings("unchecked")
    private Object buildFieldTypeSimple(Field field, String fieldName,
                                        Object fieldValue, CompareOperations compareOperation) {
        // валидация
        if (Arrays.asList(field.getType().getInterfaces()).contains(Collection.class)) {
            if (!field.getType().equals(List.class)) {
                throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT,
                        String.format("Недопустимый тип данных поля: [%s]. Поддерживается только List",
                                fieldName));
            }

            Class<?> genericType = getGenericType(field);
            if (!ALLOWED_GENERIC_TYPES.contains(genericType)) {
                throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT,
                        String.format("Неверный параметризованный тип коллекции: [%s]. Поле: [%s]",
                                genericType, fieldName));
            }

            List<Object> list = new ArrayList<>((List<Object>) fieldValue);

            switch (compareOperation) {
                case BETWEEN:
                    if (list.size() != 2) {
                        throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT,
                                String.format("Операция [%s] должна иметь 2 аргумента. Поле: [%s]",
                                        compareOperation, fieldName));
                    }
                    break;
                case IN:
                    if (list.isEmpty()) {
                        throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT,
                                String.format("Операция [%s] должна иметь хотя бы 1 аргумент. Поле: [%s]",
                                        compareOperation, fieldName));
                    }
                    break;
                default:
                    throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT,
                            String.format("Недопустимый тип данных для операции сравнения [%s]. Поле: [%s]",
                                    compareOperation, fieldName));
            }
        }

        return new SearchCriteria(fieldName, compareOperation, fieldValue);
    }

    /**
     * Получить generic type/
     *
     * @param field {@link Field}
     * @return {@link Class}
     */
    private Class<?> getGenericType(Field field) {
        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        return (Class<?>) stringListType.getActualTypeArguments()[0];
    }
}
