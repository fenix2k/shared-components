# openfeign-core

## Описание

Библиотека с классами конфигурации openfeign клиента для взаимодействия с другими сервисами

## Подключение

В pom.xml проекта должны присутствовать следующие зависимости:
```xml
<dependencies>
    <dependency>
        <groupId>ru.atc.mvd.gismu.shared2</groupId>
        <artifactId>openfeign-core</artifactId>
    </dependency>
</dependencies>
```

## Использование

### Конфигурация
1. Добавить параметры конфигурации в application.yaml.
   Можно переопределить параметры по-умолчанию и параметры для каждого сервиса.
    ```yaml
    openfeign:
        enabled: true     # признак активации (по-умолчанию: true)
        defaults:         # параметры по-умолчанию
          auth:               # параметры аутентификации (не работает пока)
            enabled: false        # признак активации
          requestOptions:     # параметры запроса
            connectTimeout: 10000 # таймаут соединения
            readTimeOut: 60000    # минимальный интервал повтора
            followRedirects: true # следовать редиректам
            retryOptions:     # параметры повторения запроса
              allowRetry: true    # повторять ли запрос в случае неудачи
              period: 100         # таймаут чтения данных при запросе
              maxPeriod: 1000     # следовать редиректам
              maxAttempts: 5      # параметры повторных запросов
          micrometerOptions:  # параметры мониторинга
            enabled: false        # признак активации
          logLevel: NONE      # уровень логирования (NONE|BASIC|HEADER|FULL)
        services:         # параметры по каждому сервису
          SERVICE_CODE:       # код сервиса
            serviceName: service_name   # наименование сервиса
            serviceUrl: http://service_name:8080  # URL сервиса
            auth:
              enabled: false
            requestOptions:
              connectTimeout: 10000
              readTimeOut: 60000
              followRedirects: true
              retryOptions:
                allowRetry: true
                period: 100
                maxPeriod: 1000
                maxAttempts: 5
            micrometerOptions:
              enabled: false
            logLevel: NONE
    ```
2. Создать интерфейс с описанием API сервиса
    ```java
    public interface SomeServiceFeignClient {
        @RequestLine("GET /api/test")
        String doRequest();
    }
    ```
   Для более простого связывания клиента с yml параметрами можно использовать аннотацию [@FeignService](../openfeign-api/src/main/java/ru/atc/mvd/gismu/shared2/communication/openfeign/api/annotation/FeignService.java)
   над интерфейсом. В аннотации также можно указать url и name.
    ```java
    @FeignService(code = "SERVICE_CODE", url = "http://service_name:8080", name = "service_name")
    public interface SomeServiceFeignClient {}
    ```
   ВНИМАНИЕ!!! Параметры аннотации переопределяют параметры yml конфигурации.
3. Создать класс конфигурации и в нём создать экземпляры клиентов
    ```java
    @Configuration
    @RequiredArgsConstructor
    public class FeignClientConfig {
        /** Билдер feign клиентов. */
        private final FeignClientBuilder feignClientBuilder;
        
        @Bean("someServiceFeignClient")
        public Test2FeignClient someServiceFeignClient() {
            return feignClientBuilder.build(SomeServiceFeignClient.class, "SERVICE_CODE");
        }
    }
    ```

### Использование
```java
@Service
@RequiredArgsConstructor
public class QueryService {
    
    private final SomeServiceFeignClient someServiceFeignClient;

    public String doQuery() {
        return someServiceFeignClient.doRequest();
    }
}
```

### Билдер FeignClientBuilder

В конструкторе принимает на вход: 
- [FeignClientPropertiesService.java](../openfeign-api/src/main/java/ru/atc/mvd/gismu/shared2/communication/openfeign/api/FeignClientPropertiesService.java) -
содержит необходимые методы для работы с yml конфигурацией
- [FeignConfigComponents.java](../openfeign-api/src/main/java/ru/atc/mvd/gismu/shared2/communication/openfeign/api/config/FeignConfigComponents.java) - 
содержит стандартные компоненты для конфигурирования feign (Client, Encoder, Decoder и т.д.)

После создания экземпляра для создания feign клиента используется метод build(), который принимает следующие параметры:
- apiType - интерфейс feign клиента (обязателен)
- serviceCode - код сервиса для получения конфигурации из application.yml (необязательно при наличии аннотации @FeignService над интерфейсом клиента или параметра clientServiceProperties)
- clientServiceProperties - локальная конфигурация feign клиента (переопределяет глобальную)
- components - локальная конфигурация компонентов feign клиента (переопределяет глобальную)

### Автоконфигурация FeignClientBuilder

Для автоконфигурации FeignClientBuilder используется класс конфигурации [DefaultFeignClientBuilderConfig.java](src/main/java/ru/atc/mvd/gismu/shared2/communication/openfeign/core/config/DefaultFeignClientBuilderConfig.java) 
и в результате создаётся FeignClientBuilder с именем "defaultFeignClientBuilder". Это имя можно использовать в качестве квалифаера при наличии более одного экземпляра билдера.

При необходимости можно создать свой класс конфигурации.