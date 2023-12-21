# Печать конфигурации для SpringBoot 

## Описание

Библиотека для вывода параметров конфигурации в консоль при старте SpringBoot приложения.

Для автоматического подключения используется механизм SpringBoot Starter.

При помощи стандартного интерфейса логгера выводит следующее:
1. Информацию о текущей временной зоне (log.level = info)
2. Список параметров конфигурации включая переменные окружения с разбивкой по источнику, профили. (log.level = info)
3. Список инициализированных бинов (log.level = debug)

Класс конфигурации [ConfigLoggerProperties.java](src/main/java/ru/atc/mvd/gismu/shared2/logging/config/logger/springboot/starter/config/properties/ConfigLoggerProperties.java)
содержит настройки вывода.

## Подключение

В pom.xml проекта должны присутствовать следующие зависимости:
```xml
<dependencies>
    <dependency>
        <groupId>ru.atc.mvd.gismu.shared2</groupId>
        <artifactId>config-logger-springboot-starter</artifactId>
    </dependency>
</dependencies>
```

## Настройка
Добавить конфигурацию в application.yaml
```yaml
logger:
    config-logger:
      enabled: true # по-умолчанию true
      disableTimeZonePrint: false # отключить печать временной зоны (по-умолчанию false)
      disableEnvVarPrint: false # отключить печать конфигурации (по-умолчанию false)
      disableBeanPrint: false   # отключить печать бинов (по-умолчанию false)
```