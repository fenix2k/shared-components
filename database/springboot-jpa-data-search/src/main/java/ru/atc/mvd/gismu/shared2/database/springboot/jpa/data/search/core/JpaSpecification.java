package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core;

import org.springframework.data.jpa.domain.Specification;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.PredicateBuilder;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.TypeCasterProvider;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.JpaDataSearchException;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Common Specification.
 *
 * @param <E> entity класс
 */
@SuppressWarnings("unused")
public class JpaSpecification<E> implements Specification<E> {

    private static final long serialVersionUID = 42L;

    /** Текущие тип сущности. */
    private final Class<E> entityClass;

    /** Критерии поиска. */
    private final transient SearchCriteria criteria;

    /** Маппер типов полей запроса. */
    private final transient TypeCasterProvider typeCasterProvider;

    /** Билдер предиката. */
    private final transient PredicateBuilder predicateBuilder;

    /** Список имен вложенных полей-сущностей. */
    private final Set<String> entityFields = new HashSet<>();

    /**
     * Конструктор.
     *
     * @param criteria критерии поиска {@link SearchCriteria}
     * @param params параметры {@link SpecificationParams}
     */
    public JpaSpecification(SearchCriteria criteria,
                            SpecificationParams<E> params) {
        if (criteria == null) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "Аргумент [criteria] не может быть null");
        }
        if (params == null) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "Аргумент [params] не может быть null");
        }
        if (params.getEntityClass() == null) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "EntityClass не может быть null");
        }
        if (params.getPredicateBuilder() == null) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "PredicateBuilder не может быть null");
        }
        if (params.getTypeCasterProvider() == null) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_ARGUMENT, "TypeCaster не может быть null");
        }

        this.entityClass = params.getEntityClass();
        this.typeCasterProvider = params.getTypeCasterProvider();
        this.predicateBuilder = params.getPredicateBuilder();
        this.criteria = criteria;
    }

    /**
     * Возвращает предикат.
     * Поддерживает join дочерних сущностей (пример поля user.name).
     * Поиск по списку пока не поддерживается.
     *
     * @param root {@link Root}
     * @param query {@link CriteriaQuery}
     * @param builder {@link CriteriaBuilder}
     * @return {@link Predicate}
     */
    @Override
    public Predicate toPredicate(final Root<E> root,
                                 final CriteriaQuery<?> query,
                                 final CriteriaBuilder builder) {
        String[] dotSplit = criteria.getKey().split("\\.");

        // Делаем так, чтобы в результате поиска все связанные сущности возвращались одним запросом
        for (String entityField : entityFields) {
            root.fetch(entityField, JoinType.LEFT);
        }
        entityFields.clear();

        if (dotSplit.length == 1) {
            Class<?> fieldClass = getFieldClass(entityClass, criteria.getKey());
            Object value = typeCasterProvider.cast(fieldClass, criteria.getValue());

            return predicateBuilder.build(builder, root, criteria.getOperation(), criteria.getKey(), value);
        } else {
            String field;
            Join<E, E> join = null;
            Class<?> fieldClass = entityClass;

            for (int i = 0; i < dotSplit.length - 1; i++) {
                field = dotSplit[i];

                JoinType joinType = isCollection(fieldClass, field) ? JoinType.LEFT : JoinType.INNER;
                join = getJoinIfExistOrCreate(join == null ? root : join, field, joinType);

                fieldClass = getFieldClass(fieldClass, field);
            }

            field = dotSplit[dotSplit.length - 1];

            fieldClass = getFieldClass(fieldClass, field);

            Object value = typeCasterProvider.cast(fieldClass, criteria.getValue());

            return predicateBuilder.build(builder, join, criteria.getOperation(), field, value);
        }
    }

    /**
     * Возвращает, является ли поле сущностью (с аннотацией @Entity).
     *
     * @param fieldClass тип поля {@link Class}
     * @return признак является ли сущностью
     */
    private boolean isEntity(Class<?> fieldClass) {
        Optional<Annotation> entity = Arrays.stream(fieldClass.getAnnotations())
                .filter(v -> v.annotationType().equals(Entity.class))
                .findFirst();
        return entity.isPresent();
    }

    /**
     * Возвращает класс поля fieldKey сущности fieldClass.
     *
     * @param fieldClass тип поля {@link Class}
     * @param fieldKey имя поля {@link String}
     * @return {@link Class}
     */
    private Class<?> getFieldClass(Class<?> fieldClass, String fieldKey) {
        try {
            Class<?> type;
            try {
                type = fieldClass.getDeclaredField(fieldKey).getType();
            } catch (NoSuchFieldException ex) {
                if (fieldClass.getSuperclass() == null) {
                    throw new NoSuchFieldException(ex.getLocalizedMessage());
                }
                type = getFieldSuperClass(fieldClass, fieldKey);
            }
            return type;
        } catch (NoSuchFieldException ex) {
            throw new JpaDataSearchException(ExceptionCodes.INVALID_SEARCH_QUERY,
                    String.format("Атрибут [%s] не найден в сущности [%s]", fieldKey, fieldClass), ex);
        }
    }

    /**
     * Возвращает суперкласс поля fieldKey сущности fieldClass.
     *
     * @param fieldClass тип поля {@link Class}
     * @param fieldKey имя поля {@link String}
     * @return {@link Class}
     * @throws NoSuchFieldException
     */
    private Class<?> getFieldSuperClass(Class<?> fieldClass, String fieldKey) throws NoSuchFieldException {
        Class<?> type;
        try {
            type = fieldClass.getSuperclass().getDeclaredField(fieldKey).getType();
        } catch (NoSuchFieldException fieldException) {
            if (fieldClass.getSuperclass().getSuperclass() == null) {
                throw new NoSuchFieldException(fieldException.getLocalizedMessage());
            }
            type = fieldClass.getSuperclass().getSuperclass().getDeclaredField(fieldKey).getType();
        }
        return type;
    }

    /**
     * Возвращает, является ли поле коллекцией.
     *
     * @param fieldClass тип поля {@link Class}
     * @param field имя поля {@link String}
     * @return признак является ли коллекцией
     */
    private boolean isCollection(Class<?> fieldClass, String field) {
        return Arrays.stream(getFieldClass(fieldClass, field).getGenericInterfaces())
                .anyMatch(val -> val.getTypeName().startsWith("java.util.Collection"));
    }

    /**
     * Возвращает существующий join, либо создаёт новый.
     *
     * @param from сущность для связи {@link From}
     * @param field поле, по которому происходит join {@link String}
     * @param joinType join type {@link JoinType}
     * @return {@link Join}
     */
    @SuppressWarnings("unchecked")
    private Join<E, E> getJoinIfExistOrCreate(From<E, E> from, String field, JoinType joinType) {
        return from.getJoins().stream()
                .filter(join -> join.getAttribute().getName().equals(field))
                .findFirst()
                .map(join -> (Join<E, E>) join)
                .orElseGet(() -> from.join(field, joinType));
    }
}
