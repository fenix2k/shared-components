package ru.atc.mvd.gismu.shared2.exceptionhandler.spring.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.atc.mvd.gismu.shared2.exceptionhandler.core.dto.CommonErrorMessageDto;

import java.util.Objects;

/**
 * DTO ошибки для вывода на UI.
 */
@Getter
@Setter
@ToString
public class ErrorMessageDto extends CommonErrorMessageDto {

    /** Ошибка. */
    private String error;

    /** Url вызываемого api. */
    private String path;

    public ErrorMessageDto(CommonErrorMessageDto commonErrorMessage, String error, String path) {
        super(commonErrorMessage);
        this.error = error;
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ErrorMessageDto)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        ErrorMessageDto that = (ErrorMessageDto) o;

        if (!Objects.equals(error, that.error)) {
            return false;
        }
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        return result;
    }
}
