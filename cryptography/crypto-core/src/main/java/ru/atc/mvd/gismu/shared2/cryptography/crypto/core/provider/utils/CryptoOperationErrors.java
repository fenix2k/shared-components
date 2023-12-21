package ru.atc.mvd.gismu.shared2.cryptography.crypto.core.provider.utils;

/**
 * Перечень ошибок криптооперации.
 */
@SuppressWarnings("unused")
public class CryptoOperationErrors {

    public static final String INVALID_INPUT_ALGORITHM = "алгоритм не указан";
    public static final String INVALID_INPUT_DATA = "входные данные не определены";
    public static final String NO_CERT_PRESENT = "сертификат не определен";
    public static final String CERT_DECODE_ERROR = "ошибка декодирования сертификата";
    public static final String ENCRYPTION_ERROR = "ошибка шифрования";
    public static final String EXTRACT_ENCRYPTION_DATA_ERROR = "ошибка извлечения зашифрованных данных";
    public static final String DECRYPTION_ERROR = "ошибка дешифрования";
    public static final String NO_KEY_PRESENT = "ключ не определен";
    public static final String INVALID_KEY = "неверный ключ";
    public static final String CERT_ADD_ERROR = "ошибка добавления сертификата";
    public static final String SIGN_GENERATION_ERROR = "ошибка генерации подписи";
    public static final String EXTRACT_SIGN_ERROR = "ошибка извлечения подписи";
    public static final String NO_SIGN_PRESENT = "подпись не определена";
    public static final String SIGN_FORMAT_ERROR = "неверный формат подписи";
    public static final String SIGN_VERIFY_ERROR = "ошибка проверки подписи";
    public static final String SIGN_ALGORITHM_NOT_FOUND = "алгоритм подписи не найден";
    public static final String HASH_ALGORITHM_NOT_FOUND = "алгоритм хеширования не найден";
    public static final String SECRET_KEY_NOT_MATCH_ALG = "секретный ключ не соответствует алгоритму";

    public static final String CERT_CREATION_FAILED = "не удалось создать сертификат";
    public static final String CRYPTO_PROVIDER_NOT_FOUND = "криптопровайдер не найден";
    public static final String ALGORITHM_PARAMS_NOT_FOUND = "параметры инициализации не найдены для алгоритма";
    public static final String ALGORITHM_NOT_FOUND = "алгоритм не найден";
}
