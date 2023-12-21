# Глобальная обработка ошибок

## Описание

Глобальный обработчик исключений для формирования унифицированного DTO ответа в случае ошибки.

## Подключение
Добавление зависимости в pox.xml:
```xml
<dependency>
    <groupId>ru.atc.mvd.gismu.shared2</groupId>
    <artifactId>exception-handler-core</artifactId>
</dependency>
```

## Компоненты
- [CommonExceptionHandlerProperties.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/config/properties/CommonExceptionHandlerProperties.java) -
класс базовых параметров конфигурации. Определяет параметры поведения обработчика исключений.
- [CommonErrorMessageDto.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/dto/CommonErrorMessageDto.java) -
общее DTO с ошибкой генерируемое в результате обработки исключения.
- [AbstractRuntimeException.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/exceptions/AbstractRuntimeException.java) -
абстрактый класс исключения. Все частные исключения должны быть унаследованы от него.
- [CommonRuntimeException.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/exceptions/CommonRuntimeException.java) -
общее исключение. Реализация AbstractRuntimeException.
- [ExceptionCode.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/ExceptionCode.java) -
интерфейс кода ошибки. Используется для описания кодов ошибок.
- [CommonExceptionCodes.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/CommonExceptionCodes.java) -
общий перечень кодов ошибок. Реализация интерфейса ExceptionCode.
- [ErrorMessageMapper.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/mapper/ErrorMessageMapper.java) -
интерфейс маппера исключения в DTO.
- [DefaultErrorMessageMapper.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/mapper/DefaultErrorMessageMapper.java) -
общая реализация интерфейса ErrorMessageMapper. Прообразовывает исключения в CommonErrorMessageDto.
- [ExceptionHandler.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/handlers/ExceptionHandler.java) -
интерфейс обработчика исключений.
- [AbstractExceptionHandler.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/handlers/AbstractExceptionHandler.java) -
абстрактная реализация интерфейса ExceptionHandler. Реализует общую логику обработки исключений.
- [DefaultExceptionHandler.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/handlers/DefaultExceptionHandler.java) -
общая реализация AbstractExceptionHandler. Перехватывает все AbstractRuntimeException.
- [ExceptionHandlerManager.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/handlers/ExceptionHandlerManager.java) -
менеджер для управления обработчиками и вызова соответствующего обработчика.

## Использование

### Конфигурация
Создать экземпляр ExceptionHandlerManager: 
```java
/**
 * Получить параметры конфигурации.
 */
public CommonExceptionHandlerProperties getExceptionHandlerManager() {
        CommonExceptionHandlerProperties properties = new CommonExceptionHandlerProperties();
        
        // Код ошибки по-умолчанию
        properties.setDefaultExceptionCode(CommonExceptionCodes.COMMON);
        // Показывать полное сообщение
        properties.setShowFullMessage(true);
        // Не показывать полное сообщение для ошибок с кодами из списка
        properties.setExcludeFullMessageFilterByCode(new HashSet<>(Arrays.asList("C0050")));
        // Не показывать полное сообщение для ошибок определённого типа
        properties.setExcludeFullMessageFilterByType(new HashSet<>(Arrays.asList("COMMON")));
        
        return properties;
}

/**
 * Получить экземпляр ExceptionHandlerManager.
 */
public ExceptionHandlerManager getExceptionHandlerManager( CommonExceptionHandlerProperties properties) {
    // Список обработчиков исключений
    List<ExceptionHandler> exceptionHandlers = Collections.unmodifiableList(Arrays.asList(new DefaultExceptionHandler()));
    // Маппер ошибки в DTO
    ErrorMessageMapper errorMessageMapper = new DefaultErrorMessageMapper(properties);
    // Создание экземпляра
    return new ExceptionHandlerManager(exceptionHandlers, errorMessageMapper);
}
```

### Использование
```java
try {
    throw new RuntimeException("Runtime Exception cause");
} catch (Exception e) {
    try {
        throw new CommonRuntimeException(CommonExceptionCodes.ARGUMENT_EXCEPTION,
                "Error message", "Сообщение об ошибке", e);
    } catch (Exception ex) {
        CommonErrorMessageDto errorMessage = exceptionHandlerManager.getErrorMessage(ex);
    }
}
```

В итоге будет получено DTO с сообщением об ошибке:
```json
{
  "message": "Error message",
  "fullMessage": "Error message. Cause = Runtime Exception cause",
  "localizedMessage": "Сообщение об ошибке",
  "type": "COMMON",
  "code": "0003",
  "status": 500,
  "timestamp": "2023-02-20T14:35:45.505952700"
}
```

## Расширение
### Реализация собственного класс исключения
В идеале каждая подсистема (библиотека) должна реализовать на минимум одни свой класс исключения и вызывать это исключение при необходимости.

Другие исключения не должны вызываться.

Класс исключения должен расширять класс [CommonRuntimeException.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/exceptions/CommonRuntimeException.java).

Пример:
```java
/**
 * Исключение подсистемы.
 */
public class ApplicationException extends CommonRuntimeException {

    public ApplicationException(ExceptionCode code, HttpStatus httpStatus, String message) {
        super(code, httpStatus, message);
    }

    public ApplicationException(ExceptionCode code, String message) {
        super(code, message);
    }

    public ApplicationException(ExceptionCode code, String message, Object detailedInfo) {
        super(code, message, detailedInfo);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(ExceptionCode code, HttpStatus httpStatus, String message, Object detailedInfo, Throwable ex) {
        super(code, httpStatus, message, detailedInfo, ex);
    }
}
```

### Реализация собственного перехватчика исключений
При необходимости подсистема (библиотека) может реализовать свой обработчик исключения.

Это может быть нужно при необходимости перехвата и обработки исключений из внешних библиотек.

Для этого необходимо расширить класс [AbstractExceptionHandler.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/handlers/AbstractExceptionHandler.java).

Пример:
```java
/**
 * Обработчик по-умолчанию.
 */
public class ApplicationExceptionHandler extends AbstractExceptionHandler
        implements ErrorExceptionHandler {

    /**
     * Тут указываем какой класс исключений перехватываем.
     */
    @Override
    public Class<? extends AbstractRuntimeException> exceptionClass() {
        return ApplicationException.class;
    }

    /**
     * (Не обязательно) Тут можно изменять свойства исключения
     */
    @Override
    public void beforeBuildErrorMessage(Exception ex) {
        super.beforeBuildErrorMessage(ex);
    }

    /**
     * (Не обязательно) Тут можно изменять свойства dto
     */
    @Override
    public void afterBuildErrorMessage(CommonErrorMessageDto errorMessage) {
        super.afterBuildErrorMessage(errorMessage);
    }
}
```

### Реализация собственных кодов исключений
Коды исключений представляют собой реализацию интерфейса [ExceptionCode.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/ExceptionCode.java),
который позволяет получить следующую информацию:
- Код ошибки - код ошибки для отображения в документации без привязки к локализации
- Тип ошибки - тип ошибки
- Код модуля - код модуля (подсистемы/библиотеки) к которому принадлежит набор кодов исключений
- Описание ошибки - краткое описание типа ошибки
- Http статус ошибки - http статус ошибки (java класс HttpStatus), который определяет с каким статусом ответит сервер.

В данной библиотеке имеется общий набор кодов исключений [CommonExceptionCodes.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/core/CommonExceptionCodes.java),
который можно использовать в общих случаях.

Если подсистема (библиотека) имеет свои специфичные коды исключений, то они быть реализованы в рамках подсистемы (библиотеки).

Пример:
```java
/**
 * Типы ошибок.
 */
@Getter
@RequiredArgsConstructor
public enum AppExceptionCodes implements ExceptionCode {

    APP_COMMON_ERROR("0001", "COMMON", "Общая ошибка приложения"),
    SOME_ERROR("0002", "COMMON", "Частная ошибка приложения", 400);

    private final String module = "APP";
    private final String code;
    private final String type;
    private final String description;
    private final HttpStatus httpStatus;
}
```

### Генерация исключения в коде:
```java
    throw new ApplicationException(AppExceptionCodes.SOME_ERROR, "Какая-то ошибка");
```

## Концепция обработки исключений
Для обеспечения единообразия обработки и учёта ошибок при разработке всех подсистем (библиотек) в рамках проекта 
должны быть выполнены следующие шаги:
1. Подсистема подключает глобальный обработчик исключений (п.1).
2. В конфигурационном файле application.yml определить уникальный префикс для микросервиса в рамках проекта (п.2).
3. Подсистема определяет специфичные для подсистемы коды исключений (п.5).
4. Подсистема определяет общий класс исключения подсистемы (п.3).
5. Все исключения генерируемые в коде проекта должны использовать исключение из п.4 с указанием кода исключения из п.3 
или общие коды исключений из данной библиотеки.
6. Для перехвата исключений сторонних библиотек необходимо реализовать собственный обработчик исключений (п.4).

Результат:
1. Единообразие http ответов.
2. Единообразие кодов ошибок. Четкая и понятная структура.
3. Расширяемость кодов ошибок.
4. Гибкое управление кодами ошибок. Общие коды и специфические для подсистемы (библиотеки).
5. Документирование ошибок.
6. В перспективе возможность локализации описания ошибок.