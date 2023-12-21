package ru.atc.mvd.gismu.shared2.exceptionhandler.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Типы ошибок.
 */
@Getter
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class CommonExceptionCodes implements ExceptionCode {

    public static final int DEFAULT_HTTP_STATUS_CODE = 500;

    // Общие ошибки (0000 - 0049)
    public static final ExceptionCode COMMON = 
            new CommonExceptionCodes("0000", "COMMON", "Общая ошибка", 500);
    public static final ExceptionCode CLASS_CAST_EXCEPTION = 
            new CommonExceptionCodes("0001", "COMMON", "Ошибка преобразования java типа");
    public static final ExceptionCode NULL_POINTER_EXCEPTION = 
            new CommonExceptionCodes("0002", "COMMON", "Обращение к объекту по null указателю");
    public static final ExceptionCode ARGUMENT_EXCEPTION = 
            new CommonExceptionCodes("0003", "COMMON", "Неверный аргумент");
    public static final ExceptionCode INVALID_OPERATION_EXCEPTION = 
            new CommonExceptionCodes("0004", "COMMON", "Невозможно совершить операцию в текущем статусе");
    public static final ExceptionCode VALIDATION_ERROR =
            new CommonExceptionCodes("0005", "COMMON", "Ошибка валидации", 400);
    public static final ExceptionCode INVALID_FORMAT =
            new CommonExceptionCodes("0006", "COMMON", "Неверный формат", 400);
    public static final ExceptionCode CONVERSION_ERROR =
            new CommonExceptionCodes("0007", "COMMON", "Ошибка конвертации");

    // Ошибки связанные с обращением в БД (0050 - 0099)
    public static final ExceptionCode ENTITY_NOT_FOUND = 
            new CommonExceptionCodes("0050", "IO", "Сущность не найдена", 400);
    public static final ExceptionCode FIELD_UNIQUE_EXCEPTION =
            new CommonExceptionCodes("0051", "IO", "Нарушение уникальности поля в БД");
    public static final ExceptionCode TOO_MANY_RESULTS =
            new CommonExceptionCodes("0052", "IO", "Размер полученного результата выше ожидаемого");

    // Ошибки связанные с файловой системой (0100 - 0149)
    public static final ExceptionCode FILE_READ_ERROR = 
            new CommonExceptionCodes("0100", "FS", "Ошибка чтения файла");
    public static final ExceptionCode FILE_WRITE_ERROR = 
            new CommonExceptionCodes("0101", "FS", "Ошибка записи в файл");
    public static final ExceptionCode FILE_ACCESS_ERROR = 
            new CommonExceptionCodes("0102", "FS", "Ошибка доступа к файлу");
    public static final ExceptionCode FILE_STORAGE_ERROR = 
            new CommonExceptionCodes("0103", "FS", "Ошибка файлового хранилища");

    // Ошибки связанные с авторизацией (0150 - 0199)
    public static final ExceptionCode UNAUTHORIZED =
            new CommonExceptionCodes("0150", "SEC", "Клиент не прошел аутентификацию", 401);
    public static final ExceptionCode PERMISSION_DENIED =
            new CommonExceptionCodes("0151'", "SEC", "Не достаточно прав доступа", 403);

    // Ошибки связанные с обменом данных по сети (0200 - 0299)
    public static final ExceptionCode MISSING_REQUEST_PARAM_VALUE =
            new CommonExceptionCodes("0200", "NET", "Недостаточно параметров в запросе");
    public static final ExceptionCode NOT_VALID_PARAM_VALUE =
            new CommonExceptionCodes("0201", "NET", "Не валидные параметры в запросе");
    public static final ExceptionCode HTTP_MESSAGE_NOT_READABLE =
            new CommonExceptionCodes("0202", "NET", "Не валидные параметры в запросе");
    public static final ExceptionCode HTTP_MESSAGE_NOT_WRITABLE =
            new CommonExceptionCodes("0203", "NET", "Не валидные параметры в запросе");
    public static final ExceptionCode HTTP_METHOD_NOT_SUPPORTED =
            new CommonExceptionCodes("0204", "NET", "HTTP метод не поддерживается");
    public static final ExceptionCode HTTP_MEDIA_TYPE_NOT_SUPPORTED =
            new CommonExceptionCodes("0205", "NET", "HTTP медиа тип не поддерживается");
    public static final ExceptionCode HTTP_MEDIA_TYPE_NOT_ACCEPTABLE =
            new CommonExceptionCodes("0206", "NET", "HTTP медиа тип не применим");
    public static final ExceptionCode MISSING_PATH_VARIABLE =
            new CommonExceptionCodes("0207", "NET", "Парамер запроса отсутствует");
    public static final ExceptionCode CONVERSION_NOT_SUPPORTED =
            new CommonExceptionCodes("0208", "NET", "Преобразование типа не поддерживается");
    public static final ExceptionCode TYPE_MISMATCH =
            new CommonExceptionCodes("0209", "NET", "Тип не соответствует");
    public static final ExceptionCode SERVLET_REQUEST_BINDING =
            new CommonExceptionCodes("0210", "NET", "Ошибка привязки сервлета");
    public static final ExceptionCode MISSING_SERVLET_REQUEST_PART =
            new CommonExceptionCodes("0211", "NET", "Отсутствует часть сервлет запроса");
    public static final ExceptionCode BIND_EXCEPTION =
            new CommonExceptionCodes("0212", "NET", "Ошибка связывания");
    public static final ExceptionCode NO_HANDLER_FOUND =
            new CommonExceptionCodes("0213", "NET", "Обработчик не найден");
    public static final ExceptionCode REMOTE_SERVER_RESPONSE_ERROR =
            new CommonExceptionCodes("0214", "NET", "Ошибка ответа удалённого сервера");
    public static final ExceptionCode REQUEST_TIMEOUT =
            new CommonExceptionCodes("0215", "NET", "Таймаут запроса");
    public static final ExceptionCode ASYNC_REQUEST_TIMEOUT =
            new CommonExceptionCodes("0216", "NET", "Таймаут асинхронного запроса");
    public static final ExceptionCode EXCEPTION_INTERNAL =
            new CommonExceptionCodes("0217", "NET", "Внутренняя ошибка");

    private final String module = "C";
    private final String code;
    private final String type;
    private final String description;
    private final int httpStatusCode;

    public CommonExceptionCodes(String code, String type, String description) {
        this.code = code;
        this.type = type;
        this.description = description;
        this.httpStatusCode = DEFAULT_HTTP_STATUS_CODE;
    }
}