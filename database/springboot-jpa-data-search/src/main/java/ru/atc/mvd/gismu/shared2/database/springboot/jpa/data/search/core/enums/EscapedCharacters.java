package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.search.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Экранированные символы.
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public enum EscapedCharacters {

    COMMA("\\\\,");

    /** Символ. */
    private final String character;
}
