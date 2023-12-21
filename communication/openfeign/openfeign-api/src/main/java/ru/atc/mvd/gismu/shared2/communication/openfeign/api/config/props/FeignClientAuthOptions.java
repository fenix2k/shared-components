package ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props;

import lombok.Data;

/**
 * Параметры аутентификации клиента.
 */
@Data
public class FeignClientAuthOptions {

    /** Включить аутентификацию. */
    private boolean enabled;

    /** Логин. */
    private String login;
}
