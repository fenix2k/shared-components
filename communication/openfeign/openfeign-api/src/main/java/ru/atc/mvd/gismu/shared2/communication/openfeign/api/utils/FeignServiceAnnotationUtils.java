package ru.atc.mvd.gismu.shared2.communication.openfeign.api.utils;

import ru.atc.mvd.gismu.shared2.communication.openfeign.api.annotation.FeignService;

import java.util.Optional;

/**
 * Вспомогательный класс для работы с аннотацией @FeignService.
 */
public class FeignServiceAnnotationUtils {

    /**
     * Получить код сервиса из serviceCode или из apiType аннотации FeignService.code().
     *
     * @param apiType класс клиента {@link Class}<{@link T}>
     * @param serviceCode код сервиса {@link String}
     * @return {@link Optional}<{@link String}>
     * @param <T> тип класса клиента
     */
    public static <T> Optional<String> getServiceCode(Class<T> apiType, String serviceCode) {
        String resultServiceCode = null;
        if (serviceCode != null && !serviceCode.isEmpty()) {
            resultServiceCode = serviceCode;
        } else if (apiType != null && apiType.isAnnotationPresent(FeignService.class)) {
            resultServiceCode = apiType.getAnnotation(FeignService.class).code();
        }
        return Optional.ofNullable(resultServiceCode);
    }

    /**
     * Получить URL сервиса из serviceUrl или из apiType аннотации FeignClient.code().
     *
     * @param apiType класс клиента {@link Class}<{@link T}>
     * @param serviceUrl url сервиса {@link String}
     * @return {@link Optional}<{@link String}>
     * @param <T> тип класса клиента
     */
    public static <T> Optional<String> getServiceUrl(Class<T> apiType, String serviceUrl) {
        String url = null;
        if (serviceUrl != null && !serviceUrl.isEmpty()) {
            url = serviceUrl;
        } else if (apiType != null && apiType.isAnnotationPresent(FeignService.class)) {
            url = apiType.getAnnotation(FeignService.class).url();
        }
        return Optional.ofNullable(url);
    }

    /**
     * Получить URL сервиса из serviceUrl или из apiType аннотации FeignClient.code().
     *
     * @param apiType класс клиента {@link Class}<{@link T}>
     * @param serviceName url сервиса {@link String}
     * @return {@link Optional}<{@link String}>
     * @param <T> тип класса клиента
     */
    public static <T> Optional<String> getServiceName(Class<T> apiType, String serviceName) {
        String name = null;
        if (serviceName != null && !serviceName.isEmpty()) {
            name = serviceName;
        } else if (apiType != null && apiType.isAnnotationPresent(FeignService.class)) {
            name = apiType.getAnnotation(FeignService.class).name();
        }
        return Optional.ofNullable(name);
    }
}
