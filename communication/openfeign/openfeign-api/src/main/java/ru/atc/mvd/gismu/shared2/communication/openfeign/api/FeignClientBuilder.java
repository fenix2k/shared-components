package ru.atc.mvd.gismu.shared2.communication.openfeign.api;

import lombok.NonNull;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.FeignConfigComponents;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props.FeignClientServiceProperties;

/**
 * Интерфейс билдера feign client.
 */
@SuppressWarnings("unused")
public interface FeignClientBuilder {

    /**
     * Создать экземпляр feign клиента.
     *
     * @param apiType тип клиента {@link Class}<{@link T}>
     * @param serviceCode код сервиса {@link String}
     * @param clientProperties параметры клиента {@link FeignClientServiceProperties}
     * @param components компоненты конфигурации feign клиента {@link FeignConfigComponents}
     * @return feign клиент {@link T}
     * @param <T> тип клиента {@link Class}<{@link T}>
     */
    <T> T build(Class<T> apiType,
                String serviceCode,
                FeignClientServiceProperties clientProperties,
                FeignConfigComponents components);

    /**
     * Создать экземпляр feign клиента.
     *
     * @param apiType тип клиента {@link Class}<{@link T}>
     * @param serviceCode код сервиса {@link String}
     * @return feign клиент {@link T}
     * @param <T> тип клиента {@link Class}<{@link T}>
     */
    <T> T build(@NonNull Class<T> apiType, String serviceCode);

    /**
     * Создать экземпляр feign клиента.
     *
     * @param apiType тип клиента {@link Class}<{@link T}>
     * @param serviceCode код сервиса {@link String}
     * @param components компоненты конфигурации feign клиента {@link FeignConfigComponents}
     * @return feign клиент {@link T}
     * @param <T> тип клиента {@link Class}<{@link T}>
     */
    <T> T build(@NonNull Class<T> apiType, String serviceCode,
                FeignConfigComponents components);

    /**
     * Создать экземпляр feign клиента.
     *
     * @param apiType тип клиента {@link Class}<{@link T}>
     * @param components компоненты конфигурации feign клиента {@link FeignConfigComponents}
     * @return feign клиент {@link T}
     * @param <T> тип клиента {@link Class}<{@link T}>
     */
    <T> T build(@NonNull Class<T> apiType,
                FeignClientServiceProperties clientServiceProperties,
                FeignConfigComponents components);

    /**
     * Создать экземпляр feign клиента.
     *
     * @param apiType тип клиента {@link Class}<{@link T}>
     * @return feign клиент {@link T}
     * @param <T> тип клиента {@link Class}<{@link T}>
     */
    <T> T build(@NonNull Class<T> apiType);

//    /**
//     * Создать экземпляр feign клиента.
//     *
//     * @param apiType тип клиента {@link Class}<{@link T}>
//     * @param serviceCode код сервиса {@link String}
//     * @param clientProperties параметры клиента {@link FeignClientServiceProperties}
//     * @param encoder кодировщик {@link Encoder}
//     * @param decoder декодер {@link Decoder}
//     * @param errorDecoder декодер ошибок {@link ErrorDecoder}
//     * @param requestInterceptors интерсепторы {@link Collection}<{@link RequestInterceptor}>
//     * @param capabilities доп. функции {@link Collection}<{@link Capability}>
//     * @return feign клиент {@link T}
//     * @param <T> тип клиента {@link Class}<{@link T}>
//     */
//    <T> T build(Class<T> apiType,
//                String serviceCode,
//                FeignClientServiceProperties clientProperties,
//                Encoder encoder, Decoder decoder, ErrorDecoder errorDecoder,
//                Collection<RequestInterceptor> requestInterceptors,
//                Collection<Capability> capabilities);

//    /**
//     * Создать экземпляр feign клиента.
//     *
//     * @param apiType тип клиента {@link Class}<{@link T}>
//     * @param serviceCode код сервиса {@link String}
//     * @param clientProperties параметры клиента {@link FeignClientServiceProperties}
//     * @return feign клиент {@link T}
//     * @param <T> тип клиента {@link Class}<{@link T}>
//     */
//    <T> T build(Class<T> apiType, String serviceCode, FeignClientServiceProperties clientProperties);
//
//    /**
//     * Создать экземпляр feign клиента.
//     *
//     * @param apiType тип клиента {@link Class}<{@link T}>
//     * @param serviceCode код сервиса {@link String}
//     * @param clientProperties параметры клиента {@link FeignClientServiceProperties}
//     * @param requestInterceptors интерсепторы {@link Collection}<{@link RequestInterceptor}>
//     * @return feign клиент {@link T}
//     * @param <T> тип клиента {@link Class}<{@link T}>
//     */
//    <T> T build(Class<T> apiType, String serviceCode, FeignClientServiceProperties clientProperties,
//                Collection<RequestInterceptor> requestInterceptors);
//
//    /**
//     * Создать экземпляр feign клиента.
//     *
//     * @param apiType тип клиента {@link Class}<{@link T}>
//     * @param serviceCode код сервиса {@link String}
//     * @param clientProperties параметры клиента {@link FeignClientServiceProperties}
//     * @param encoder кодировщик {@link Encoder}
//     * @param decoder декодер {@link Decoder}
//     * @param requestInterceptors интерсепторы {@link Collection}<{@link RequestInterceptor}>
//     * @return feign клиент {@link T}
//     * @param <T> тип клиента {@link Class}<{@link T}>
//     */
//    <T> T build(Class<T> apiType, String serviceCode, FeignClientServiceProperties clientProperties,
//                Encoder encoder, Decoder decoder,
//                Collection<RequestInterceptor> requestInterceptors);
}
