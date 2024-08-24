package ru.dlabs71.library.exception.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.dlabs71.library.exception.type.ErrorCode;
import ru.dlabs71.library.exception.type.ErrorLevel;

@Getter
@Setter
public class BusinessLogicServiceException extends ServiceException {

    private ErrorLevel level;
    private Object data;

    public BusinessLogicServiceException(
        String description,
        ErrorCode errorCode,
        ErrorLevel level,
        Object data,
        @NonNull Throwable cause
    ) {
        super(description, errorCode, cause);
        this.level = level != null ? level : ErrorLevel.ERROR;
        this.data = data;
    }

    public BusinessLogicServiceException(
        String description,
        ErrorCode errorCode,
        ErrorLevel level,
        Object data
    ) {
        super(description, errorCode);
        this.level = level != null ? level : ErrorLevel.ERROR;
        this.data = data;
    }

    public static BusinessLogicServiceException build(String description) {
        return new BusinessLogicServiceException(description, null, null, null);
    }

    public static BusinessLogicServiceException build(String description, Throwable throwable) {
        return new BusinessLogicServiceException(description, null, null, null, throwable);
    }

    public static BusinessLogicServiceException build(ErrorCode errorCode) {
        return new BusinessLogicServiceException(null, errorCode, null, null);
    }

    public static BusinessLogicServiceException build(ErrorCode errorCode, Throwable throwable) {
        return new BusinessLogicServiceException(null, errorCode, null, null, throwable);
    }

    public static BusinessLogicServiceException build(String description, ErrorLevel errorLevel) {
        return new BusinessLogicServiceException(description, null, errorLevel, null);
    }

    public static BusinessLogicServiceException build(ErrorCode errorCode, ErrorLevel errorLevel) {
        return new BusinessLogicServiceException(null, errorCode, errorLevel, null);
    }

    public static BusinessLogicServiceException build(String description, ErrorLevel errorLevel, Throwable throwable) {
        return new BusinessLogicServiceException(description, null, errorLevel, null, throwable);
    }

    public static BusinessLogicServiceException build(ErrorCode errorCode, ErrorLevel errorLevel, Throwable throwable) {
        return new BusinessLogicServiceException(null, errorCode, errorLevel, null, throwable);
    }
}
