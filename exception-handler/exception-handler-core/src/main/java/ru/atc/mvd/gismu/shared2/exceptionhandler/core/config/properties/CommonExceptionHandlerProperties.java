package ru.atc.mvd.gismu.shared2.exceptionhandler.core.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.CommonExceptionCodes;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.ExceptionCode;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Конфигурация.
 */
@Getter
@Setter
@NoArgsConstructor
public class CommonExceptionHandlerProperties {

    /** Тип ошибки по-умолчанию. */
    private ExceptionCode defaultExceptionCode = CommonExceptionCodes.COMMON;

    /** Отображать ли полное сообщение об ошибке. */
    private boolean isShowFullMessage;

    /** Фильтр по типу для кодов ошибок для которых не будет отображаться полное сообщение. */
    private Set<String> excludeFullMessageFilterByType = new HashSet<>();

    /** Фильтр по коду для кодов ошибок для которых не будет отображаться полное сообщение. */
    private Set<String> excludeFullMessageFilterByCode = new HashSet<>();

    public Set<String> getExcludeFullMessageFilterByType() {
        return Collections.unmodifiableSet(excludeFullMessageFilterByType);
    }

    public Set<String> getExcludeFullMessageFilterByCode() {
        return Collections.unmodifiableSet(excludeFullMessageFilterByCode);
    }

    /**
     * Добавить значиения excludeFullMessageFilterByType.
     *
     * @param excludeFullMessageFilterByType {@link Set}<{@link String}>
     */
    public void setExcludeFullMessageFilterByType(Set<String> excludeFullMessageFilterByType) {
        this.excludeFullMessageFilterByType.addAll(excludeFullMessageFilterByType);
    }

    /**
     * Добавить значиения excludeFullMessageFilterByCode.
     *
     * @param excludeFullMessageFilterByCode {@link Set}<{@link String}>
     */
    public void setExcludeFullMessageFilterByCode(Set<String> excludeFullMessageFilterByCode) {
        this.excludeFullMessageFilterByCode.addAll(excludeFullMessageFilterByCode);
    }
}
