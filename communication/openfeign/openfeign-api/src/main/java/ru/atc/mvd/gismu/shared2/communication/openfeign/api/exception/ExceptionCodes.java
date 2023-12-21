package ru.atc.mvd.gismu.shared2.communication.openfeign.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Коды ошибок.
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionCodes {

    REQUEST_ERROR("Ошибка запроса"),
    BUILD_ERROR("Ошибка создания экземпляра feign клиента");

    private final String description;
}
