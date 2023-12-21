# Библиотека глобальной обработки ошибок с компонентами для Spring

## Описание

Библиотека глобальной обработки ошибок с компонентами для Spring. 
Содержит необходимые классы и зависимости для интеграции со Spring.

## Подключение
Добавление зависимости в pox.xml:
```xml
<dependency>
    <groupId>ru.atc.mvd.gismu.shared2</groupId>
    <artifactId>exception-handler-spring-core</artifactId>
</dependency>
```

## Компоненты
- [ExceptionHandlerProperties.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/spring/core/config/properties/ExceptionHandlerProperties.java) -
класс параметров конфигурации Spring. Определяет параметры поведения обработчика исключений.
- [ExceptionHandlerConfig.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/spring/core/config/ExceptionHandlerConfig.java) -
класс конфигурации Spring. Создаёт необходимые бины.
- [ErrorMessageDto.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/spring/core/dto/ErrorMessageDto.java) -
общее DTO с ошибкой генерируемое в результате обработки исключения.
- [ExceptionCodes.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/spring/core/ExceptionCodes.java) -
общий перечень кодов ошибок Spring. Реализация интерфейса ExceptionCode.

## Использование

### Конфигурация
Производиться через конфигурационный файл application.yml
```yaml
global-exception-handler:
  enabled: true             # включает глобальный обработчик (по-умолчанию: true)
  defaultExceptionCode: COMMON # тип ошибки по-умолчанию
  isShowFullMessage: true # показывать ли полный текст ошибки
  excludeFullMessageFilterByType: # фильтр по типу для кодов ошибок для которых не будет отображаться полное сообщение
    - ERROR_TYPE
  excludeFullMessageFilterByCode: # фильтр по коду для кодов ошибок для которых не будет отображаться полное сообщение
    - ERROR_CODE
```

Добавить класс обработчик:
```java
/**
 * Формирование HTTP ответов в случае ошибок.
 * Перехватывает ЛЮБОЕ исключение.
 */
@Slf4j
public class ExceptionHandlerControllerAdviser extends ResponseEntityExceptionHandler {
    
    private final ExceptionHandlerManager exceptionHandlerManager;

    public ExceptionHandlerControllerAdviser(ExceptionHandlerManager exceptionHandlerManager) {
        super();
        this.exceptionHandlerManager = exceptionHandlerManager;
        log.info("ErrorExceptionHandlers: {}", exceptionHandlerManager.getErrorExceptionHandlerNames());
    }

    /**
     * Общий обработчик исключений.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDto> handleException(WebRequest request, Exception ex) {
        ErrorMessageDto errorMessageDto = buildErrorMessage(request, ex);
        return new ResponseEntity<>(errorMessageDto, new HttpHeaders(), errorMessageDto.getStatus());
    }

    /** При необохимости переобределить методы класса. */

    private ErrorMessageDto buildErrorMessage(WebRequest request, Exception ex) {
        log.info("Handle exception: type=[{}], message=[{}]", ex.getClass(), ex.getLocalizedMessage());
        log.debug("Exception stacktrace: ", ex);

        CommonErrorMessageDto commonErrorMessage = exceptionHandlerManager.getErrorMessage(ex);

        String error = null;
        HttpStatus httpStatus = HttpStatus.resolve(commonErrorMessage.getStatus());
        if (httpStatus != null) {
            error = httpStatus.getReasonPhrase();
        }

        ErrorMessageDto errorMessageDto = new ErrorMessageDto(commonErrorMessage, error, request.getContextPath());
        errorMessageDto.setPath(request.getContextPath());
        log.info("Error response: {}", errorMessageDto);
        return errorMessageDto;
    }
}
```

Добавить новый класс конфигурации: 
```java
/**
 * Глобальный перехватчик исключений.
 */
@ControllerAdvice
@Configuration
@ConditionalOnProperty(value = "exception-handler.enabled")
@Import(ExceptionHandlerConfig.class)
@Slf4j
public class ExceptionHandlerAdviserConfig extends ExceptionHandlerControllerAdviser {

    public ExceptionHandlerAdviserConfig(ExceptionHandlerManager exceptionHandlerManager) {
        super(exceptionHandlerManager);
        log.info("ExceptionHandler is enabled");
    }
}
```

### Использование

Далее все исключения будут перехватываться и оборачивать в унифицированные сообщение [ErrorMessageDto.java](src/main/java/ru/atc/mvd/gismu/shared2/exceptionhandler/spring/core/dto/ErrorMessageDto.java):
```json
{
  "message": "Error message",
  "fullMessage": "Error message. Cause = Runtime Exception cause",
  "localizedMessage": "Сообщение об ошибке",
  "type": "COMMON",
  "code": "0003",
  "status": 400,
  "error": "Bad Request",
  "timestamp": "2023-02-20T14:35:45.505952700",
  "path": "uri=/api/person/1"
}
```

## Расширение

### Реализация собственного класс исключения
Необходимо создать необходимые бины.
См. [реализация-собственного-класс-исключения](../exception-handler-spring-core/README.md#реализация-собственного-класс-исключения).

### Реализация собственного перехватчика исключений
Необходимо создать необходимые бины.
См. [реализация-собственного-перехватчика-исключений](../exception-handler-spring-core/README.md#реализация-собственного-перехватчика-исключений).

### Реализация собственных кодов исключений
См. [реализация-собственных-кодов-исключений](../exception-handler-spring-core/README.md#реализация-собственных-кодов-исключений).


### Генерация исключения в коде:
```java
    throw new ApplicationException(AppExceptionCodes.SOME_ERROR, "Какая-то ошибка");
```