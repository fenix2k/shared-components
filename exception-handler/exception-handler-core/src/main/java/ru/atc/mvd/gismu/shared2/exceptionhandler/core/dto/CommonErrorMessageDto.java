package ru.atc.mvd.gismu.shared2.exceptionhandler.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO ошибки для вывода на UI.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class CommonErrorMessageDto {

    /** Сообщение об ошибке. */
    private String message;

    /** Подробное сообщение об ошибке. */
    private String fullMessage;

    /** Сообщение об ошибке для отображения пользователю. */
    private String localizedMessage;

    /** Дополнительная информация. Может быть любым объектом. */
    private Object details;

    /** Тип сообщения. */
    private String type;

    /** Код ошибки */
    private String code;

    /** Код http статуса. */
    private int status;

    /** Время возникновения (милисекунды). */
    private String timestamp;

    public CommonErrorMessageDto(CommonErrorMessageDto commonErrorMessage) {
        this.message = commonErrorMessage.getMessage();
        this.fullMessage = commonErrorMessage.getFullMessage();
        this.localizedMessage = commonErrorMessage.getLocalizedMessage();
        this.details = commonErrorMessage.getDetails();
        this.type = commonErrorMessage.getType();
        this.code = commonErrorMessage.getCode();
        this.status = commonErrorMessage.getStatus();
        this.timestamp = commonErrorMessage.getTimestamp();
    }
}
