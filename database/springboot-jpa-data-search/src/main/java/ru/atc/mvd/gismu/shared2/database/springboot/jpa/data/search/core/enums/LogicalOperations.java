package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Логические операторы для соединения поисковых параметров.
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum LogicalOperations {

    AND("AND", 2, 2),
    OR("OR", 1, 2);

    /** Токен операции. */
    private final String token;
    /** Старшинство. */
    private final int precedence;
    /** Кол-во аргументов. */
    private final int numOfArgs;

    /**
     * Получить мапу.
     *
     * @return Map
     */
    public static Map<String, LogicalOperations> getOperations() {
        return Arrays.stream(LogicalOperations.values())
                .collect(Collectors.toMap(LogicalOperations::getToken, v -> v));
    }

    /**
     * Сравнить старшинство.
     *
     * @param currOp {@link LogicalOperations}
     * @param prevOp {@link LogicalOperations}
     * @return boolen
     */
    public static boolean isHigherPrecedence(LogicalOperations currOp, LogicalOperations prevOp) {
        return prevOp != null && prevOp.precedence >= currOp.precedence;
    }
}
