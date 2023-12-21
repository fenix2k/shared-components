package ru.atc.mvd.gismu.shared2.communication.openfeign.core;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.FeignClientPropertiesService;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props.FeignClientProperties;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.props.FeignClientServiceProperties;

import java.util.Optional;

/**
 * Сервис для работы с параметрами клиента.
 */
@Service
@RequiredArgsConstructor
public class DefaultFeignClientPropertiesService implements FeignClientPropertiesService {

    /** Общая конфигурация. */
    private final FeignClientProperties feignClientProperties;

    @Override
    public FeignClientServiceProperties getAndMergeServiceProperties(String serviceCode,
                                                                     FeignClientServiceProperties clientServiceProperties) {
        return mergeServiceProperties(
                getServicePropertiesByCode(serviceCode).orElse(null),
                clientServiceProperties);
    }

    /**
     * Получить параметры клиента по коду сервиса из общей конфигурации.
     *
     * @param serviceCode код сервиса {@link String}
     * @return {@link Optional}<{@link FeignClientServiceProperties}>
     */
    private Optional<FeignClientServiceProperties> getServicePropertiesByCode(String serviceCode) {
        if (feignClientProperties.getServices() != null && !feignClientProperties.getServices().isEmpty()) {
            return Optional.ofNullable(feignClientProperties.getServices().getOrDefault(serviceCode, null));
        }
        return Optional.empty();
    }

    /**
     * Объединяет общую конфигурацию и локальную конфигурацию клиента.
     *
     * @param commonServiceProps yml конфигурация клиента {@link FeignClientServiceProperties}
     * @param localServiceProps конфигурация клиента {@link FeignClientServiceProperties}
     * @return {@link FeignClientServiceProperties}
     */
    private FeignClientServiceProperties mergeServiceProperties(FeignClientServiceProperties commonServiceProps,
                                                                FeignClientServiceProperties localServiceProps) {
        FeignClientServiceProperties currentServiceProps = commonServiceProps;
        if (currentServiceProps == null) {
            currentServiceProps = new FeignClientServiceProperties();
            currentServiceProps.setAuth(this.feignClientProperties.getDefaults().getAuth());
            currentServiceProps.setRequestOptions(this.feignClientProperties.getDefaults().getRequestOptions());
            currentServiceProps.setMicrometerOptions(this.feignClientProperties.getDefaults().getMicrometerOptions());
            currentServiceProps.setLogLevel(this.feignClientProperties.getDefaults().getLogLevel());
        }
        if (localServiceProps == null) {
            return currentServiceProps;
        }

        if (localServiceProps.getServiceUrl() != null) {
            currentServiceProps.setServiceUrl(localServiceProps.getServiceUrl());
        }
        if (localServiceProps.getServiceName() != null) {
            currentServiceProps.setServiceName(localServiceProps.getServiceName());
        }
        if (localServiceProps.getAuth() != null) {
            currentServiceProps.setAuth(localServiceProps.getAuth());
        }
        if (localServiceProps.getRequestOptions() != null) {
            currentServiceProps.setRequestOptions(localServiceProps.getRequestOptions());
        }
        if (localServiceProps.getMicrometerOptions() != null) {
            currentServiceProps.setMicrometerOptions(localServiceProps.getMicrometerOptions());
        }
        if (localServiceProps.getLogLevel() != null) {
            currentServiceProps.setLogLevel(localServiceProps.getLogLevel());
        }

        return currentServiceProps;
    }
}
