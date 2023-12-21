# Автоконфигурация Swagger для SpringBoot

## Описание

Предоставляет предопределённую конфигурацию Swagger для SpringBoot.

## Подключение

В pom.xml проекта должны присутствовать следующие зависимости:
```xml
<dependencies>
    <dependency>
        <groupId>ru.atc.mvd.gismu.shared2</groupId>
        <artifactId>swagger-autoconfig-spring-core</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-autoconfigure</artifactId>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-ui</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## Настройка
Swagger будет доступен по URL:
- Spring Web: /swagger-ui/index.html
- Spring Webflux: /webjars/swagger-ui/index.html

Добавить конфигурацию в application.yaml
```yaml
swagger-autoconfig:
  enabled: true
  openApiTitle: Title # Заголовок
  auth-config: # Конфигурация аутентификации
    authType: NONE # возможные варианты авторизации: NONE, BASIC (логин/пароль), BEARER (JWT токен)
  servers-config: # Конфигурация серверов
    useLocalServer: true # Добавить локальный сервер
    servers: # Список серверов
        - url: http://gateway:8080/
          description: Gateway Server
  cors-сonfig:
    enabled: false # включить автоконфигурирования CORS (разрешить все)
```