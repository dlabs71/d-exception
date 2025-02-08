package ru.dlabs71.library.exception.exception;

import java.io.Serializable;
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
public final class BusinessLogicServiceException extends ServiceException {

    private ErrorLevel level;
    private Serializable data;

    /**
     * Constructor of the class.
     *
     * @param message   message explain cause of an exception.
     * @param errorCode error code.
     *                  See documentation to the {@linkplain ServiceException#ServiceException(String, ErrorCode)}
     * @param level     a specific error level. If the level is null,
     *                  then it will be assigned a {@link CommonErrorLevel#ERROR} value.
     * @param data      extra data for HTTP response
     * @param cause     a throwable object - cause of exception
     */
    public BusinessLogicServiceException(
        String message,
        ErrorCode errorCode,
        ErrorLevel level,
        Serializable data,
        @NonNull Throwable cause
    ) {
        super(message, errorCode, cause);
        this.level = level != null ? level : CommonErrorLevel.ERROR;
        this.data = data;
    }

    /**
     * Constructor of the class.
     *
     * @param message   message explain cause of an exception.
     * @param errorCode error code.
     *                  See documentation to the {@linkplain ServiceException#ServiceException(String, ErrorCode)}
     * @param level     a specific error level. If the level is null,
     *                  then it will be assigned a {@link CommonErrorLevel#ERROR} value.
     * @param data      extra data for HTTP response
     */
    public BusinessLogicServiceException(
        String message,
        ErrorCode errorCode,
        ErrorLevel level,
        Serializable data
    ) {
        super(message, errorCode);
        this.level = level != null ? level : CommonErrorLevel.ERROR;
        this.data = data;
    }

    public static BusinessLogicServiceException build(String message) {
        return new BusinessLogicServiceException(message, null, null, null);
    }

    public static BusinessLogicServiceException build(String message, Throwable throwable) {
        return new BusinessLogicServiceException(message, null, null, null, throwable);
    }

    public static BusinessLogicServiceException build(ErrorCode errorCode) {
        return new BusinessLogicServiceException(null, errorCode, null, null);
    }

    public static BusinessLogicServiceException build(ErrorCode errorCode, Throwable throwable) {
        return new BusinessLogicServiceException(null, errorCode, null, null, throwable);
    }

    public static BusinessLogicServiceException build(String message, ErrorLevel errorLevel) {
        return new BusinessLogicServiceException(message, null, errorLevel, null);
    }

    public static BusinessLogicServiceException build(ErrorCode errorCode, ErrorLevel errorLevel) {
        return new BusinessLogicServiceException(null, errorCode, errorLevel, null);
    }

    public static BusinessLogicServiceException build(String message, ErrorLevel errorLevel, Throwable throwable) {
        return new BusinessLogicServiceException(message, null, errorLevel, null, throwable);
    }

    public static BusinessLogicServiceException build(ErrorCode errorCode, ErrorLevel errorLevel, Throwable throwable) {
        return new BusinessLogicServiceException(null, errorCode, errorLevel, null, throwable);
    }
}
