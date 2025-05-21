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
 * You can specify level (or type) alert with {@link ErrorLevel}. Also, you can specify extra data for a
 * client application.
 *
 * <p>You should specify {@link ErrorCode}. It can be used to specify a text message.
 *
 * <p><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
@Getter
@Setter
public class BusinessLogicException extends ServiceException {

    private ErrorLevel level;
    private Serializable data;

    /**
     * Constructor of the class.
     *
     * @param errorCode error code.
     *                  See documentation to the {@linkplain ServiceException#ServiceException(String, ErrorCode)}
     * @param level     a specific error level. If the level is null,
     *                  then it will be assigned a {@link CommonErrorLevel#ERROR} value.
     * @param data      extra data for HTTP response
     * @param cause     a throwable object - cause of exception
     */
    public BusinessLogicException(
        ErrorCode errorCode,
        ErrorLevel level,
        Serializable data,
        @NonNull Throwable cause
    ) {
        super(errorCode.name(), errorCode, cause);
        this.level = level != null ? level : CommonErrorLevel.ERROR;
        this.data = data;
    }

    /**
     * Constructor of the class.
     *
     * @param errorCode error code.
     *                  See documentation to the {@linkplain ServiceException#ServiceException(String, ErrorCode)}
     * @param level     a specific error level. If the level is null,
     *                  then it will be assigned a {@link CommonErrorLevel#ERROR} value.
     * @param data      extra data for HTTP response
     */
    public BusinessLogicException(
        ErrorCode errorCode,
        ErrorLevel level,
        Serializable data
    ) {
        super(errorCode.name(), errorCode);
        this.level = level != null ? level : CommonErrorLevel.ERROR;
        this.data = data;
    }

    public static BusinessLogicException build(ErrorCode errorCode) {
        return new BusinessLogicException(errorCode, null, null);
    }

    public static BusinessLogicException build(ErrorCode errorCode, Throwable throwable) {
        return new BusinessLogicException(errorCode, null, null, throwable);
    }

    public static BusinessLogicException build(ErrorCode errorCode, ErrorLevel errorLevel) {
        return new BusinessLogicException(errorCode, errorLevel, null);
    }

    public static BusinessLogicException build(ErrorCode errorCode, ErrorLevel errorLevel, Throwable throwable) {
        return new BusinessLogicException(errorCode, errorLevel, null, throwable);
    }

    public static ServiceException build(String message) {
        throw new UnsupportedOperationException(
            "The string message is not supported in BusinessLogicException. "
            + "Please use ErrorCode to specify the message."
        );
    }

    public static ServiceException build(String message, Throwable throwable) {
        throw new UnsupportedOperationException(
            "The string message is not supported in BusinessLogicException. "
            + "Please use ErrorCode to specify the message."
        );
    }
}
