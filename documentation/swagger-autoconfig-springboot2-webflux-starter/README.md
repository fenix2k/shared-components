# Автоконфигурация Swagger для SpringBoot2 (spring-webflux)

## Описание

Предоставляет предопределённую конфигурацию Swagger для SpringBoot2 (spring-webflux).

## Подключение
В pom.xml проекта должны присутствовать следующие зависимости:
```xml
<dependencies>
    <dependency>
        <groupId>ru.atc.mvd.gismu.shared2</groupId>
        <artifactId>swagger-autoconfig-webflux</artifactId>
    </dependency>
</dependencies>
```

## Настройка
Добавить конфигурацию в application.yaml
```yaml
swagger-autoconfig:
  enabled: true
  uri: swagger # URL для доступа к swagger. По-умолчанию "swagger" (необязательно) 
  openApiTitle: Title # Заголовок
  auth-config: # Конфигурация аутентификации
    authType: NONE # возможные варианты авторизации: NONE, BASIC (логин/пароль), BEARER (JWT токен)
  servers-config: # Конфигурация серверов
    useLocalServer: true # Добавить локальные сервер
    servers: # Список серверов
        - url: http://gateway:8080/
          description: Gateway Server
  cors-config:
    enabled: true # разрешить автоконфигурирование CORS (разрешить все)
```

Swagger будет доступен по URL: /swagger