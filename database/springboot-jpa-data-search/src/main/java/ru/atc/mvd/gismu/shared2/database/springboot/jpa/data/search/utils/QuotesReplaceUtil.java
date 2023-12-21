package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.utils;

/**
 * Класс для работы со спецсимволом \\" (кавычка) в поисковом запросе.
 */
public final class QuotesReplaceUtil {

    private static final String QUOTE = "\"";
    private static final String SHIELD_QUOTE = "\\\\\"";

    private QuotesReplaceUtil() {
    }

    /**
     * Экранирование кавычки.
     *
     * @param fieldValue {@link String}
     * @return {@link String} с заменённым символом
     */
    public static String replaceQuotes(Object fieldValue) {
        return fieldValue == null ? null : fieldValue.toString().replaceAll(QUOTE, SHIELD_QUOTE);
    }

    /**
     * Деэкранирование.
     *
     * @param fieldValue {@link String} со спецсимволом
     * @return {@link String} с кавычками
     */
    public static String replaceSpecialCharacter(Object fieldValue) {
        return fieldValue == null ? null : fieldValue.toString().replaceAll(SHIELD_QUOTE, QUOTE);
    }
}
