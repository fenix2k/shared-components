# Глобальный обработчик исключений для SpringBoot2 (spring-webflux)

## Описание

Глобальный обработчик исключений для формирования унифицированного HTTP ответа в случае ошибки для SpringBoot2 (spring-webflux).

## Подключение
Добавление зависимости в pox.xml:
```xml
<dependency>
    <groupId>ru.atc.mvd.gismu.shared2</groupId>
    <artifactId>exception-handler-springboot2-webflux-starter</artifactId>
</dependency>
```

## Компоненты
- [spring.factories](src/main/resources/META-INF/spring.factories) - 
конфигурация для автоконфигурирования SpringBoot (механизм стартера).
- [ExceptionHandlerControllerAdviser.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/springboot2/webflux/ExceptionHandlerControllerAdviser.java) -
конфигурация для перехвата любых исключений и формирования HTTP ответов в случае ошибок.
- [ExceptionHandlerAdvisorConfig.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/springboot2/webflux/ExceptionHandlerAdvisorConfig.java) -
конфигурация глобального обработчика для Spring.

## Использование

### Конфигурация, использование и расширение
См. [конфигурация, использование и расширение](../exception-handler-spring-core/README.md#конфигурация)