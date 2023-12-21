package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Операторы группировки для соединения поисковых параметров.
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum GroupOperations {

    LEFT_BRACKET("("),
    RIGHT_BRACKET(")");

    /** Токен операции. */
    private final String token;

    /**
     * Получить мапу.
     *
     * @return Map
     */
    public static Map<String, GroupOperations> getOperations() {
        return Arrays.stream(GroupOperations.values())
                .collect(Collectors.toMap(GroupOperations::getToken, v -> v));
    }
}
