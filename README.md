# Общие библиотеки, используемые при реализации компонент

## Описание

Общие компоненты версии 2. Набор общих библиотек, обязательных для использования во всех проектах, 
для обеспечения качества и единообразия кодовой базы.

ВНИМАНИЕ! Библиотеки не должны иметь зависимостей от конкретного проекта или подсистемы.

## Перечень компонент

- [code-analysis-rules](code-analysis-rules/README.md) - подключение плагинов для сборки проекта и управление их версиями, конфигурирование статического анализа исходного кода (checkstyle, spotbugs)
  - [checkstyle-rules](code-analysis-rules/checkstyle-rules/README.md) - правила для статического анализатор стиля исходного кода Checkstyle
  - [spotbugs-rules](code-analysis-rules/spotbugs-rules/README.md) - правила для статического анализатор байт-кода Spotbugs
- [build-tools](build-tools/README.md) - подключение плагинов для сборки проекта и управление их версиями, конфигурирование статического анализа исходного кода (checkstyle, spotbugs)
- [dependency-management](dependency-management/README.md) - модуль управления версиями зависимостей
  - [dependency-management-base](dependency-management/dependency-management-base/README.md) - базовые версии зависимостей
  - [dependency-management-springboot2](dependency-management/dependency-management-springboot2/README.md) - версии зависимостей для spring-boot2
- [exception-handler](exception-handler/README.md) - модуль глобальной обработки исключений
  - [exception-handler-core](exception-handler/exception-handler-core/README.md) - базовая реализация глобального обработчика исключений
  - [exception-handler-spring-core](exception-handler/exception-handler-spring-core/README.md) - глобальный обработчика исключений для spring-boot2
  - [exception-handler-springboot2-web-starter](exception-handler/exception-handler-springboot2-web-starter/README.md) - стартер для глобального обработчика исключений для spring-boot2
  - [exception-handler-springboot2-webflux-starter](exception-handler/exception-handler-springboot2-webflux-starter/README.md) - стартер для глобального обработчика исключений для spring-boot2 webflux
- [documentation](documentation/README.md) - модуль компонент для документирования
  - [swagger-autoconfig-spring-core](documentation/swagger-autoconfig-spring-core/README.md) - swagger для spring-boot
  - [swagger-autoconfig-springboot2-web-starter](documentation/swagger-autoconfig-springboot2-web-starter/README.md) - стартер swagger для spring-boot2
  - [swagger-autoconfig-springboot2-webflux-starter](documentation/swagger-autoconfig-springboot2-webflux-starter/README.md) - стартер swagger для spring-boot2 webflux
- [logging](logging/README.md) - модуль с компонентами для логирования
  - [config-logger-springboot-starter](logging/config-logger-springboot-starter/README.md) - библиотека для вывода параметров конфигурации в консоль при старте SpringBoot приложения
  - [trace-logger-springboot-starter](logging/trace-logger-springboot-starter/README.md) - библиотека для трассировки выполнения методов с использованием Spring AOP
- [database](database/README.md) - модуль с компонентами для работы с базами данных
  - [springboot-jpa-data-core](database/springboot-jpa-data-core/README.md) - библиотека предоставляет набор интерфейсов и абстрактных сервисов для организации общего подхода для работы с БД используя JPA
  - [springboot-jpa-data-search](database/springboot-jpa-data-search/README.md) - библиотека позволяет реализовать гибкий поиск по атрибутам сущностей с помощью Hibernate Specification Api
- [cryptography](cryptography/README.md) - модуль с компонентами для криптографии
  - [crypto-core](cryptography/crypto-core/README.md) - библиотека для выполнения криптографических операций
- [communication](communication/README.md) - модуль компонентов для коммуникации с другими сервисами
  - [openfeign](communication/openfeign/README.md) - модуль с библиотеками для работы с openfeign
    - [openfeign-api](communication/openfeign/openfeign-api/README.md) - библиотека api интерфейсов для работы с openfeign
    - [openfeign-core](communication/openfeign/openfeign-core/README.md) - библиотека с классами конфигурации openfeign клиента для взаимодействия с другими сервисами
    - [openfeign-springboot-starter](communication/openfeign/openfeign-springboot-starter/README.md) - библиотека для автоматического подключения openfeign-core к SpringBoot через stater
  - [kafka](communication/kafka) - модуль с библиотеками для работы с kafka
    - [kafka-api](communication/kafka/kafka-api/README.md) - библиотека api интерфейсов для работы с kafka
    - [kafka-core](communication/kafka/kafka-core/README.md) - библиотека для взаимодействия с другими сервисами через kafka

## Оглавление
- [Компиляция и сборка](#компиляция-и-сборка)
- [Использование](#использование)

## Компиляция и сборка

### Требования

Для сборки исходных кодов необходимы:
- JDK 8
- Maven версии 3.6 и выше
- доступ во внешнюю сеть к maven central репозиторию

### Сборка проекта из исходных кодов

Сборка компонента производится из корневого каталога проекта с использованием команды:
```shell
$ mvn clean install
```

### Результаты сборки

Артефакты, являющиеся результатами сборки, находятся в каталогах target модулей проекта и в локальном maven-репозитории.

## Использование

Результаты сборки данных модулей должны использоваться во всех проектах для обеспечения единообразия реализации 
тех или иных функциональных возможностей системы или обеспечения единообразного поведения системы.

### Наследование
1. Если необходимо подключить только механизмы сборки проекта, то необходимо в качестве родителя добавить [build-tools](build-tools/README.md).
    ```xml
    <parent>
      <groupId>ru.atc.mvd.gismu.shared2</groupId>
      <artifactId>build-tools</artifactId>
      <version>x.x.x</version>
    </parent>
    ```
2. Если дополнительно нужно использовать общие версии зависимостей, то можно:
   - напрямую унаследоваться от [dependency-management-base](dependency-management/dependency-management-base/README.md) 
     или другого компонента в составе [dependency-management](dependency-management/README.md)
    ```xml
    <parent>
      <groupId>ru.atc.mvd.gismu.shared2</groupId>
      <artifactId>dependency-management-base</artifactId>
      <version>x.x.x</version>
    </parent>
    ```
   - унаследоваться от [build-tools](build-tools/README.md), а потом импортировать нужны dependency-management pom
    ```xml
    <project>
        <parent>
          <groupId>ru.atc.mvd.gismu.shared2</groupId>
          <artifactId>build-tools</artifactId>
          <version>x.x.x</version>
        </parent>
        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>ru.atc.mvd.gismu.shared2</groupId>
                    <artifactId>dependency-management-base</artifactId>
                    <version>x.x.x</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
            </dependencies>
        </dependencyManagement>
    </project>
    ```
3. Использовать отдельные библиотеки.
    ```xml
    <dependency>
      <groupId>ru.atc.mvd.gismu.shared2</groupId>
      <artifactId>exception-handler-core</artifactId>
      <version>x.x.x</version>
    </dependency>
    ```