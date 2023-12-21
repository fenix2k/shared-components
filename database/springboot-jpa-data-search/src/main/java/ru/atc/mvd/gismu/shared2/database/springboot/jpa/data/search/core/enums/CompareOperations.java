package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

/**
 * Операторы сравнения атрибутов.
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum CompareOperations {

    EQUALS(":", 1),
    EQUALS_IGNORE_CASE("@", 1),
    NOT_EQUALS("!", 1),
    NOT_EQUALS_IGNORE_CASE("!@", 1),
    GREATER(">", 1),
    GREATER_OR_EQUALS(">:", 1),
    LESS("<", 1),
    LESS_OR_EQUALS("<:", 1),
    STARTS("*:", 1),
    STARTS_IGNORE_CASE("*@", 1),
    ENDS(":*", 1),
    ENDS_IGNORE_CASE("@*", 1),
    CONTAINS("~", 1),
    CONTAINS_IGNORE_CASE("~@", 1),
    IN("-", -1),
    BETWEEN("--", 2);

    /** Токен операции. */
    private final String token;
    /** Кол-во аргументов. */
    private final int numOfArgs;

    /**
     * Возвращает операцию по символу операции.
     *
     * @param operation {@link String}
     * @return {@link Optional}<{@link CompareOperations}>
     */
    public static Optional<CompareOperations> getOperation(final String operation) {
        return Arrays.stream(CompareOperations.values())
                .filter(v -> v.getToken().equalsIgnoreCase(operation))
                .findFirst();
    }

    /**
     * Возвращает массив токенов операций сравнения.
     *
     * @return String[]
     */
    public static String[] getOperations() {
        return Arrays.stream(CompareOperations.values())
                .map(CompareOperations::getToken)
                .sorted((s1, s2) -> s2.length() - s1.length())
                .toArray(String[]::new);
    }
}
