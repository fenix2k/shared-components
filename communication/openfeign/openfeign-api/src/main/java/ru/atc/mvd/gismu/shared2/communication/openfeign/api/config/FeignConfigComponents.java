package ru.atc.mvd.gismu.shared2.communication.openfeign.api.config;

import feign.Capability;
import feign.Client;
import feign.Contract;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;

/**
 * Параметры feign.
 */
@Getter
@Builder
public class FeignConfigComponents {

    /** Клиент. */
    private final Client client;
    /** Контракт. */
    private final Contract contract;
    /** Энкодер. */
    private final Encoder encoder;
    /** Декодер. */
    private final Decoder decoder;
    /** Декодер не 2хх ответов. */
    private final ErrorDecoder errorDecoder;
    /** Логер. */
    private final Logger logger;
    /** Интерсепторы запроса. */
    private final Collection<RequestInterceptor> requestInterceptors;
    /** Доп. функции. */
    private final Collection<Capability> capabilities;
}
