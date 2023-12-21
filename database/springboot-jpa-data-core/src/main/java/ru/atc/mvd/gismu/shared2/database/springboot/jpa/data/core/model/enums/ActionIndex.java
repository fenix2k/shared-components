package ru.atc.mvd.gismu.shared2.database.springboot.jpa.data.core.model.enums;

/**
 * Значения индикатора изменения строки в таблице.
 */
public enum ActionIndex {

    /** Создана новая записи. */
    I,

    /** Запись была обновлена. */
    U,

    /** Запись была удалена (помечена как удалённая). */
    D
}