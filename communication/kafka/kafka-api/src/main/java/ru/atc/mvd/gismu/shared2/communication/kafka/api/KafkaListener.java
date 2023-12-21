package ru.atc.mvd.gismu.shared2.communication.kafka.api;

import java.util.Map;

/**
 * Общий интерфейс для всех слушателей топиков.
 */
public interface KafkaListener {

    /**
     * Получить и обработать сообщение.
     *
     * @param messageString сообщение
     * @param headers заголовки
     */
    void receive(String messageString, Map<String, Object> headers);
}
