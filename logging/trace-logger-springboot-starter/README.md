# Библиотека для трассировки выполнения методов с использованием Spring AOP.

## Описание
Предоставляет инструменты для логирования вызовов методов с использованием Spring AOP

## Подключение
В pom.xml проекта должны присутствовать следующие зависимости:
```xml
<dependency>
    <groupId>ru.atc.mvd.gismu.shared2</groupId>
    <artifactId>trace-logger-springboot-starter</artifactId>
</dependency>
```

## Использование
1. В файле application.yaml добавить настройку:
```yaml
logging:
  trace-logger.enabled: true
```
Для применения изменений необходим перезапуск сервиса!

2. Там же необходимо для выбранных классов выставить уровень логирования TRACE:
```yaml
logging:
  level:
    ru.atc.mvd.gismu.shared2.trace.logger: TRACE
```

3. Пометить методы, для которых необходимо обеспечить трассировку, аннотацией @TraceLogger

### Параметры аннотации @TraceLogger
- showShortClassName - если true, то отображает имя класса и метода в укороченном виде (по-умолчанию true)
- showReturnValue - если true, то при успешном завершении метода отображает возвращаемое значение
- excludeArgIndex - перечень индексов параметров метода, которые необходимо исключить из трассировки