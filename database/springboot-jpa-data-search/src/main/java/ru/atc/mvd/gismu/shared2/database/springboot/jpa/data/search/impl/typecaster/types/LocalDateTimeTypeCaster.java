package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.impl.typecaster.types;

import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.api.TypeCaster;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.exception.JpaDataSearchException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * TypeCaster для типа LocalDateTime.class.
 */
public class LocalDateTimeTypeCaster implements TypeCaster {

    private static final List<String> PATTERNS = new ArrayList<>();
    private static final DateTimeFormatter FORMATTER;

    static {
        PATTERNS.add("yyyy-MM-dd HH:mm:ss");
        PATTERNS.add("yyyy-MM-dd HH:mm:ss.SSS");

        DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
        formatterBuilder.appendOptional(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        PATTERNS.forEach(p -> formatterBuilder.appendOptional(DateTimeFormatter.ofPattern(p)));

        FORMATTER = formatterBuilder.toFormatter(Locale.ENGLISH);
    }

    @Override
    public Class<?> getType() {
        return LocalDateTime.class;
    }

    @Override
    public Object cast(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return toInstantOrNull(value)
                    .map(instant -> LocalDateTime.ofInstant(instant, ZoneId.systemDefault()))
                    .orElse(LocalDateTime.parse(value.toString(), FORMATTER));
        } catch (DateTimeParseException ex) {
            throw new JpaDataSearchException(ExceptionCodes.TYPE_CAST,
                    String.format("Ошибка преобразования к типу [%s]: значение [%s] не соответствует формату [%s]",
                            getType(), value, PATTERNS));
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
