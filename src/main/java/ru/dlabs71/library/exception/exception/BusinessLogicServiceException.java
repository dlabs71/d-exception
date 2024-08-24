package ru.dlabs71.library.exception.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.dlabs71.library.exception.type.CommonErrorLevel;
import ru.dlabs71.library.exception.type.ErrorCode;
import ru.dlabs71.library.exception.type.ErrorLevel;

/**
 * Business Logic Exception - it's exception occurs then an application logic is broken for a reason of
 * incorrect user actions or data. Usually for a client application this error displays as alert with a text.
 * You can specify level (or type) alert with {@link ErrorCode}. Also, you can specify extra data for a
 * client application.
 *
 * <p>
 * <div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
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
        this.level = level != null ? level : CommonErrorLevel.ERROR;
        this.data = data;
    }

    public BusinessLogicServiceException(
        String description,
        ErrorCode errorCode,
        ErrorLevel level,
        Object data
    ) {
        super(description, errorCode);
        this.level = level != null ? level : CommonErrorLevel.ERROR;
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
