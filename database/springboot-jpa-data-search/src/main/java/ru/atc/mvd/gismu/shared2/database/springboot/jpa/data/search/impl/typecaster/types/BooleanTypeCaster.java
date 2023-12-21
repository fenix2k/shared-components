package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.TypeCaster;

/**
 * TypeCaster для типа boolean.class.
 */
public class BooleanTypeCaster implements TypeCaster {

    @Override
    public Class<?> getType() {
        return boolean.class;
    }

    @Override
    public Object cast(Object value) {
        return Boolean.parseBoolean(value.toString());
    }
}
