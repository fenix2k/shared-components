package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.TypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.JpaDataSearchException;

import java.util.UUID;

/**
 * TypeCaster для типа UUID.class.
 */
public class UUIDTypeCaster implements TypeCaster {

    @Override
    public Class<?> getType() {
        return UUID.class;
    }

    @Override
    public Object cast(Object value) {
        try {
            return UUID.fromString(value.toString());
        } catch (IllegalArgumentException ex) {
            throw new JpaDataSearchException(ExceptionCodes.TYPE_CAST,
                    String.format("Ошибка преобразования к типу [%s]: значение [%s]",
                            getType(), value));
        }
    }
}
