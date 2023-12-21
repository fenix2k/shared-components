package ru.atc.mvd.gismu.shared2.communication.openfeign.core.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Capability;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.FeignClientBuilder;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.FeignClientPropertiesService;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.annotation.FeignGlobalCapability;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.annotation.FeignGlobalInterceptor;
import ru.atc.mvd.gismu.shared2.communication.openfeign.api.config.FeignConfigComponents;
import ru.atc.mvd.gismu.shared2.communication.openfeign.core.DefaultFeignClientBuilder;
import ru.atc.mvd.gismu.shared2.communication.openfeign.core.component.DefaultErrorDecoder;

import java.time.ZoneId;
import java.util.Collection;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Конфигурация бинов FeignClientBuilder по-умолчанию.
 */
@Configuration
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class DefaultFeignClientBuilderConfig {

    private final ObjectProvider<HttpMessageConverterCustomizer> httpMessageConverterCustomizers;
    private final Collection<RequestInterceptor> requestInterceptors;
    private final Collection<Capability> capabilities;

    public DefaultFeignClientBuilderConfig(ObjectProvider<HttpMessageConverterCustomizer> httpMessageConverterCustomizers,
                                           Collection<RequestInterceptor> requestInterceptors,
                                           Collection<Capability> capabilities) {
        this.httpMessageConverterCustomizers = httpMessageConverterCustomizers;
        this.requestInterceptors = requestInterceptors == null ? null : requestInterceptors.stream()
                .filter(v -> v.getClass().isAnnotationPresent(FeignGlobalInterceptor.class))
                .collect(Collectors.toList());
        this.capabilities = capabilities == null ? null : capabilities.stream()
                .filter(v -> v.getClass().isAnnotationPresent(FeignGlobalCapability.class))
                .collect(Collectors.toList());
    }

    /**
     * Bean ObjectMapper.
     *
     * @return {@link ObjectMapper}
     */
    @Bean("defaultFeignObjectMapper")
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    /**
     * Bean Encoder.
     *
     * @param objectMapper {@link ObjectMapper}
     * @return {@link Encoder}
     */
    @Bean("defaultFeignEncoder")
    public Encoder encoder(@Qualifier("defaultFeignObjectMapper") ObjectMapper objectMapper) {
        HttpMessageConverter<Object> jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new SpringEncoder(objectFactory);
    }

    /**
     * Bean Decoder.
     *
     * @param objectMapper {@link ObjectMapper}
     * @return {@link Decoder}
     */
    @Bean("defaultFeignDecoder")
    public Decoder decoder(@Qualifier("defaultFeignObjectMapper") ObjectMapper objectMapper) {
        HttpMessageConverter<Object> jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory, httpMessageConverterCustomizers));
    }

    /**
     * Bean ErrorDecoder.
     *
     * @return {@link ErrorDecoder}
     */
    @Bean("defaultFeignErrorDecoder")
    public ErrorDecoder errorDecoder() {
        return new DefaultErrorDecoder();
    }

    /**
     * Bean Logger.
     *
     * @return {@link Logger}
     */
    @Bean("defaultFeignLogger")
    public Logger logger() {
        return new Slf4jLogger();
    }

    /**
     * Bean компонентов feign.
     *
     * @param encoder encoder {@link Encoder}
     * @param decoder decoder {@link Decoder}
     * @param errorDecoder errorDecoder {@link ErrorDecoder}
     * @param logger logger {@link Logger}
     * @return {@link FeignConfigComponents}
     */
    @Bean("defaultFeignConfigComponents")
    public FeignConfigComponents feignConfigComponents(@Qualifier("defaultFeignEncoder") Encoder encoder,
                                                       @Qualifier("defaultFeignDecoder") Decoder decoder,
                                                       @Qualifier("defaultFeignErrorDecoder") ErrorDecoder errorDecoder,
                                                       @Qualifier("defaultFeignLogger") Logger logger) {
        return FeignConfigComponents.builder()
                .encoder(encoder)
                .decoder(decoder)
                .errorDecoder(errorDecoder)
                .logger(logger)
                .requestInterceptors(requestInterceptors)
                .capabilities(capabilities)
                .build();
    }

    /**
     * FeignClientBuilder по-умолчанию.
     *
     * @param feignClientPropertiesService {@link FeignClientPropertiesService}
     * @param feignConfigComponents {@link FeignConfigComponents}
     * @return {@link FeignClientBuilder}
     */
    @Bean("defaultFeignClientBuilder")
    public FeignClientBuilder defaultFeignClientBuilder(FeignClientPropertiesService feignClientPropertiesService,
                                                        @Qualifier("defaultFeignConfigComponents")
                                                        FeignConfigComponents feignConfigComponents) {
        return new DefaultFeignClientBuilder(
                feignClientPropertiesService,
                feignConfigComponents);
    }
}
