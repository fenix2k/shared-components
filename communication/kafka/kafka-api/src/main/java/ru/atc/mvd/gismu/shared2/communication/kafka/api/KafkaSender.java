package ru.atc.mvd.gismu.shared2.communication.kafka.api;

/**
 * Общий интерфейс для отправки сообщений в топик.
 */
public interface KafkaSender {

    /**
     * Отправить сообщение.
     *
     * @param msg исходящие данные {@link String}
     */
    void send(String msg);
}
