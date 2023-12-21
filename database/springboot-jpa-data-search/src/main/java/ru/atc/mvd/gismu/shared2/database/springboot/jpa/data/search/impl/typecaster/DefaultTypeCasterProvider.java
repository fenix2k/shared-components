package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.TypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.TypeCasterProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Провайдер преобразователей типа.
 */
public class DefaultTypeCasterProvider implements TypeCasterProvider {

    private final Map<Class<?>, TypeCaster> typeCasterMap;

    public DefaultTypeCasterProvider(List<TypeCaster> typeCasters) {
        this.typeCasterMap = typeCasters.stream()
                .collect(Collectors.toMap(TypeCaster::getType, v -> v));
    }

    @Override
    public void addTypeCaster(TypeCaster typeCaster) {
        if (typeCaster != null && typeCaster.getType() != null) {
            this.typeCasterMap.put(typeCaster.getType(), typeCaster);
        }
    }

    @Override
    public Object cast(Class<?> fieldType, Object value) {
        if (fieldType.isInstance(value)) {
            return value;
        }

        Object result = value;

        if (typeCasterMap.containsKey(fieldType)) {
            if (Arrays.asList(value.getClass().getInterfaces()).contains(List.class)) {
                result = castList(fieldType, value);
            } else {
                result = typeCasterMap.get(fieldType).cast(value);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private Object castList(Class<?> fieldType, Object value) {
        List<Object> list = new ArrayList<>((List<Object>) value);

        if (list.stream().anyMatch(fieldType::isInstance)) {
            return value;
        }

        return list.stream()
                .map(v -> typeCasterMap.get(fieldType).cast(v))
                .collect(Collectors.toList());
    }
}
