package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.TypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.JpaDataSearchException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

/**
 * TypeCaster для типа Date.class.
 */
public class DateTypeCaster implements TypeCaster {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public Class<?> getType() {
        return Date.class;
    }

    @Override
    public Object cast(Object value) {
        try {
            return toInstantOrNull(value)
                    .map(Date::from)
                    .orElse(new SimpleDateFormat(DATE_FORMAT).parse(value.toString()));
        } catch (ParseException ex) {
            throw new JpaDataSearchException(ExceptionCodes.TYPE_CAST,
                    String.format("Ошибка преобразования к типу [%s]: значение [%s] не соответствует формату [%s]",
                            getType(), value, DATE_FORMAT));
        }
    }

    private Optional<Instant> toInstantOrNull(Object value) {
        try {
            long timestamp = Long.parseLong(value.toString());
            return Optional.of(Instant.ofEpochSecond(timestamp));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
