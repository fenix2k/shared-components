package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.utils;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.exception.JpaDataAccessException;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Содержит вспомогательные методы для реализации JPA сервиса.
 */
@SuppressWarnings("unused")
public class ServiceUtils {

    /**
     * Переписать не NULL атрибуты сущности target атрибутами сущности source.
     *
     * @param target target {@link E}
     * @param source source {@link E}
     * @param <E> {@link E}
     */
    public static <E> void mergeNotNullObjectsFields(E target, E source) {
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object targetValue = getEntityFieldValue(field, target);
            Object sourceValue = getEntityFieldValue(field, source);
            Object value = (sourceValue != null) ? sourceValue : targetValue;

            setEntityFieldValue(field, target, value);
        }
    }

    /**
     * Получить entity из HibernateProxy.
     *
     * @param proxied {@link E}
     * @param <E> {@link E}
     * @return {@link E}
     */
    @SuppressWarnings("unchecked")
    public static <E> E hibernateEntityUnproxy(E proxied) {
        E entity = proxied;
        if (entity instanceof HibernateProxy) {
            Hibernate.initialize(entity);
            entity = (E) ((HibernateProxy) entity)
                    .getHibernateLazyInitializer()
                    .getImplementation();
        }
        return entity;
    }

    /**
     * Производит поверхностное копирование объекта.
     * Вложенные сущности также копируются.
     *
     * @param entity сущность, которую нужно скопировать {@link E}
     * @return результат {@link E}
     * @param <E> сущность
     */
    @SuppressWarnings("unchecked")
    public static <E> E copyObject(E entity) {
        Class<?> clazz = entity.getClass();
        E newEntity;

        try {
            newEntity = (E) clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException ex) {
            throw new JpaDataAccessException(ExceptionCodes.CONVERSION_ERROR,
                    String.format("Не удалось создать экземпляр класса [%s]. Ошибка: %s",
                            clazz, ex.getLocalizedMessage()), ex);
        }

        while (clazz != null) {
            copyObjectFields(entity, newEntity, clazz);
            clazz = clazz.getSuperclass();
        }

        return newEntity;
    }

    /**
     * Производить поверхностное копирование атрибутов одной сущности в другую.
     * Вложенные сущности копируются.
     *
     * @param entity сущность, которую нужно скопировать {@link E}
     * @param newEntity сущность, в которую нужно скопировать {@link E}
     * @param clazz тип
     * @param <E> сущность
     */
    public static <E> void copyObjectFields(E entity, E newEntity, Class<?> clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Entity.class) != null) {
                setEntityFieldValue(field, newEntity, copyObject(getEntityFieldValue(field, entity)));
            } else {
                setEntityFieldValue(field, newEntity, getEntityFieldValue(field, entity));
            }
        }
    }

    /**
     * Получить значение поля объекта.
     *
     * @param field поля типа {@link Field}
     * @param entity сущность {@link E}
     * @return значение поля {@link Object}
     * @param <E> тип сущности
     */
    public static <E> Object getEntityFieldValue(Field field, E entity) {
        try {
            return field.get(entity);
        } catch (IllegalAccessException ex) {
            throw new JpaDataAccessException(ExceptionCodes.CONVERSION_ERROR,
                    String.format("Не удалось получить значение поля [%s] объекта [%s]. Ошибка: %s",
                            field.getName(), entity, ex.getLocalizedMessage()), ex);
        }
    }

    /**
     * Установить значение поля объекта.
     *
     * @param field поля типа {@link Field}
     * @param entity сущность {@link E}
     * @param value значение поля {@link Object}
     * @param <E> тип сущности
     */
    public static <E> void setEntityFieldValue(Field field, E entity, Object value) {
        try {
            field.set(entity, value);
        } catch (IllegalAccessException ex) {
            throw new JpaDataAccessException(ExceptionCodes.CONVERSION_ERROR,
                    String.format("Не удалось установить значение поля [%s] объекта [%s]. Ошибка: %s",
                            field.getName(), entity, ex.getLocalizedMessage()), ex);
        }
    }
}
