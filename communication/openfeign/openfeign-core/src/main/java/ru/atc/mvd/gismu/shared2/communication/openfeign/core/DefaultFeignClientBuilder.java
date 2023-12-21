package ru.atc.mvd.gismu.shared2.communication.openfeign.core;

import feign.Capability;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.slf4j.Slf4jLogger;
import lombok.Getter;
import lombok.NonNull;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.FeignClientBuilder;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.FeignClientPropertiesService;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.FeignConfigComponents;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props.FeignClientServiceProperties;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.exception.ExceptionCodes;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.exception.FeignClientException;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.utils.FeignServiceAnnotationUtils;
import ru.atc.mvd.gismu.shared2.communication.openfeign.core.component.DefaultRetryer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Билдер feign клиента с настройками по умолчанию.
 */
@Getter
@SuppressWarnings("unused")
public class DefaultFeignClientBuilder implements FeignClientBuilder {

    /** Сервис параметров. */
    private final FeignClientPropertiesService feignClientPropertiesService;
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

    /**
     * Конструктор.
     *
     * @param feignClientPropertiesService сервис параметров {@link FeignClientPropertiesService}
     * @param components компоненты клиента {@link FeignConfigComponents}
     */
    public DefaultFeignClientBuilder(@NonNull FeignClientPropertiesService feignClientPropertiesService,
                                     @NonNull FeignConfigComponents components) {
        this.feignClientPropertiesService = feignClientPropertiesService;
        this.client = components.getClient();
        this.contract = components.getContract();
        this.encoder = components.getEncoder();
        this.decoder = components.getDecoder();
        this.errorDecoder = components.getErrorDecoder();
        this.logger = components.getLogger();
        this.requestInterceptors = components.getRequestInterceptors();
        this.capabilities = components.getCapabilities();
    }

    @Override
    public <T> T build(@NonNull Class<T> apiType,
                       String serviceCode,
                       FeignClientServiceProperties clientServiceProperties,
                       FeignConfigComponents components) {
        if (components == null) {
            return build(apiType, serviceCode, clientServiceProperties,
                    null, null, null, null, null, null);
        }
        return build(apiType, serviceCode, clientServiceProperties,
                components.getContract(),
                components.getEncoder(),
                components.getDecoder(),
                components.getErrorDecoder(),
                components.getRequestInterceptors(),
                components.getCapabilities());
    }

    @Override
    public <T> T build(@NonNull Class<T> apiType, String serviceCode) {
        return build(apiType, serviceCode, null, null);
    }

    @Override
    public <T> T build(@NonNull Class<T> apiType, String serviceCode,
                       FeignConfigComponents components) {
        return build(apiType, serviceCode, null, components);
    }

    @Override
    public <T> T build(@NonNull Class<T> apiType,
                       FeignClientServiceProperties clientServiceProperties,
                       FeignConfigComponents components) {
        return build(apiType, null, clientServiceProperties, components);
    }

    @Override
    public <T> T build(@NonNull Class<T> apiType) {
        return build(apiType, null);
    }

    /**
     * Билдер клиента.
     *
     * @param apiType класс клиента {@link Class}<{@link T}>
     * @param serviceCode код сервиса {@link String}
     * @param clientServiceProperties локальная конфигурация клиента {@link FeignClientServiceProperties}
     * @param encoder энкодер {@link Encoder}
     * @param decoder декодер {@link Decoder}
     * @param errorDecoder декодер ошибок {@link ErrorDecoder}
     * @param requestInterceptors requestInterceptors {@link Collection}<{@link RequestInterceptor}>
     * @param capabilities capabilities {@link Collection}<{@link Capability}>
     * @return {@link String}
     * @param <T> тип класса клиента
     */
    private <T> T build(@NonNull Class<T> apiType,
                       String serviceCode,
                       FeignClientServiceProperties clientServiceProperties,
                       Contract contract,
                       Encoder encoder, Decoder decoder, ErrorDecoder errorDecoder,
                       Collection<RequestInterceptor> requestInterceptors,
                       Collection<Capability> capabilities) {
        // Получение кода сервиса из serviceCode или аннотации @FeignService apiType
        String resultServiceCode = FeignServiceAnnotationUtils.getServiceCode(apiType, serviceCode)
                .orElseThrow(() -> new FeignClientException(ExceptionCodes.BUILD_ERROR,
                        String.format("[%s] serviceCode is not present", apiType.getName())));

        // Получение сводной конфигурации и объединение с локальным конфигом.
        FeignClientServiceProperties serviceProperties = feignClientPropertiesService
                .getAndMergeServiceProperties(resultServiceCode, clientServiceProperties);

        // Установка URL (поиск в параметрах, если не нашли, то в аннотации @FeignService apiType)
        serviceProperties.setServiceUrl(FeignServiceAnnotationUtils
                .getServiceUrl(apiType, serviceProperties.getServiceUrl())
                .orElseThrow(() -> new FeignClientException(ExceptionCodes.BUILD_ERROR,
                                String.format("[%s] serviceUrl is not present", apiType.getName())))
        );
        // Установка Name (поиск в параметрах, если не нашли, то в аннотации @FeignService apiType)
        serviceProperties.setServiceName(FeignServiceAnnotationUtils
                .getServiceName(apiType, serviceProperties.getServiceName()).orElse(null));

        Feign.Builder builder = Feign.builder()
                .options(new Request.Options(
                        serviceProperties.getRequestOptions().getConnectTimeout(), TimeUnit.MILLISECONDS,
                        serviceProperties.getRequestOptions().getReadTimeOut(), TimeUnit.MILLISECONDS,
                        serviceProperties.getRequestOptions().isFollowRedirects())
                )
                .logLevel(serviceProperties.getLogLevel());

        // Client
        if (this.client != null) {
            builder.client(this.client);
        }
        // Contract
        builder.contract(contract != null ? contract : this.contract);
        // Encoder
        builder.encoder(encoder != null ? encoder : this.encoder);
        // Decoder
        builder.decoder(decoder != null ? decoder : this.decoder);
        // ErrorDecoder
        builder.errorDecoder(errorDecoder != null ? errorDecoder : this.errorDecoder);
        // Retry
        if (serviceProperties.getRequestOptions().getRetryOptions().isAllowRetry()) {
            builder.retryer(new DefaultRetryer(
                    serviceProperties.getRequestOptions().getRetryOptions().getPeriod(),
                    serviceProperties.getRequestOptions().getRetryOptions().getMaxPeriod(),
                    serviceProperties.getRequestOptions().getRetryOptions().getMaxAttempts())
            );
        } else {
            builder.retryer(Retryer.NEVER_RETRY);
        }
        // Logger
        builder.logger(this.logger != null ? this.logger : new Slf4jLogger(apiType.getName()));
        // RequestInterceptors
        List<RequestInterceptor> mergedRequestInterceptors = mergeRequestInterceptors(requestInterceptors);
        if (!mergedRequestInterceptors.isEmpty()) {
            builder.requestInterceptors(mergedRequestInterceptors);
        }
        // Capabilities
        mergeCapabilities(capabilities)
                .forEach(builder::addCapability);

        return builder.target(apiType, serviceProperties.getServiceUrl());
    }

    /**
     * Объединяет общую конфигурацию RequestInterceptor и локальную.
     *
     * @param requestInterceptors {@link Collection}<{@link RequestInterceptor}>
     * @return {@link List}<{@link RequestInterceptor}>
     */
    private List<RequestInterceptor> mergeRequestInterceptors(Collection<RequestInterceptor> requestInterceptors) {
        List<RequestInterceptor> merged = new ArrayList<>();
        merged.addAll(this.requestInterceptors != null ? this.requestInterceptors : new ArrayList<>());
        merged.addAll(requestInterceptors != null ? requestInterceptors : new ArrayList<>());
        return merged;
    }

    /**
     * Объединяет общую конфигурацию Capability и локальную.
     *
     * @param capabilities {@link Collection}<{@link Capability}>
     * @return {@link List}<{@link Capability}>
     */
    private List<Capability> mergeCapabilities(Collection<Capability> capabilities) {
        List<Capability> merged = new ArrayList<>();
        merged.addAll(this.capabilities != null ? this.capabilities : new ArrayList<>());
        merged.addAll(capabilities != null ? capabilities : new ArrayList<>());
        return merged;
    }
}
