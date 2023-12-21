# openfeign-api

## Описание

Api интерфейсы для работы с корпоративным openfeign клиентом

## Подключение

В pom.xml проекта должны присутствовать следующие зависимости:
```xml
<dependencies>
    <dependency>
        <groupId>ru.atc.mvd.gismu.shared2</groupId>
        <artifactId>openfeign-api</artifactId>
    </dependency>
</dependencies>
```

## Компоненты

### Конфигурация
- [FeignClientProperties.java](src/main/java/ru/atc/mvd/gismu/shared2/communication/openfeign/api/config/props/FeignClientProperties.java) -
содержит параметры конфигурации openfeign (по-умолчанию и конкретно по каждому сервису)

### Аннотации
- [FeignService.java](src/main/java/ru/atc/mvd/gismu/shared2/communication/openfeign/api/annotation/FeignService.java) -
позволяет связывать интерфейс feign клиента с yml конфигурацией, а также указывать некоторые параметры (url, name)
- [FeignGlobalInterceptor.java](src/main/java/ru/atc/mvd/gismu/shared2/communication/openfeign/api/annotation/FeignGlobalInterceptor.java) -
позволяет пометить класс Interceptor как общий для всех клиентов feign
- [FeignGlobalCapability.java](src/main/java/ru/atc/mvd/gismu/shared2/communication/openfeign/api/annotation/FeignGlobalCapability.java) -
  позволяет пометить класс Capability как общий для всех клиентов feign

### Интерфейсы
- [FeignClientPropertiesService.java](src/main/java/ru/atc/mvd/gismu/shared2/communication/openfeign/api/FeignClientPropertiesService.java) -
сервис для работы с FeignClientProperties
- [FeignClientBuilder.java](src/main/java/ru/atc/mvd/gismu/shared2/communication/openfeign/api/FeignClientBuilder.java) -
билдер для создания экземпляров openfeign клиента
