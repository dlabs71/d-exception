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
 * <br><br><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
@Getter
@Setter
public class BusinessLogicException extends ServiceException {

    private final ErrorLevel level;
    private final Serializable data;

    /**
     * Constructor of the class.
     *
     * @param errorCode       error code.
     *                        See documentation to the
     *                        {@linkplain ServiceException#ServiceException(String, ErrorCode, Object...)}
     * @param level           a specific error level. If the level is null,
     *                        then it will be assigned a {@link CommonErrorLevel#ERROR} value.
     * @param data            extra data for HTTP response
     * @param cause           a throwable object - cause of exception
     * @param codeMessageArgs parameters for substitution in the message template from the error code.
     */
    public BusinessLogicException(
        ErrorCode errorCode,
        ErrorLevel level,
        Serializable data,
        @NonNull Throwable cause,
        Object... codeMessageArgs
    ) {
        super(errorCode.name(), errorCode, cause, codeMessageArgs);
        this.level = level != null ? level : CommonErrorLevel.ERROR;
        this.data = data;
    }

    /**
     * Constructor of the class.
     *
     * @param errorCode       error code.
     *                        See documentation to the
     *                        {@linkplain ServiceException#ServiceException(String, ErrorCode, Object...)}
     * @param level           a specific error level. If the level is null,
     *                        then it will be assigned a {@link CommonErrorLevel#ERROR} value.
     * @param data            extra data for HTTP response
     * @param codeMessageArgs parameters for substitution in the message template from the error code.
     */
    public BusinessLogicException(
        ErrorCode errorCode,
        ErrorLevel level,
        Serializable data,
        Object... codeMessageArgs
    ) {
        super(errorCode.name(), errorCode, codeMessageArgs);
        this.level = level != null ? level : CommonErrorLevel.ERROR;
        this.data = data;
    }

    public static BusinessLogicException build(ErrorCode errorCode, Object... codeMessageArgs) {
        return new BusinessLogicException(errorCode, null, null, codeMessageArgs);
    }

    public static BusinessLogicException build(ErrorCode errorCode, Throwable throwable, Object... codeMessageArgs) {
        return new BusinessLogicException(errorCode, null, null, throwable, codeMessageArgs);
    }

    public static BusinessLogicException build(ErrorCode errorCode, ErrorLevel errorLevel, Object... codeMessageArgs) {
        return new BusinessLogicException(errorCode, errorLevel, null, codeMessageArgs);
    }

    public static BusinessLogicException build(
        ErrorCode errorCode,
        ErrorLevel errorLevel,
        Throwable throwable,
        Object... codeMessageArgs
    ) {
        return new BusinessLogicException(errorCode, errorLevel, null, throwable, codeMessageArgs);
    }

    /**
     * This method is not supported in BusinessLogicException.
     */
    public static ServiceException build(String message, Object... codeMessageArgs) {
        throw new UnsupportedOperationException(
            "The string message is not supported in BusinessLogicException. "
            + "Please use ErrorCode to specify the message."
        );
    }

    /**
     * This method is not supported in BusinessLogicException.
     */
    public static ServiceException build(String message, Throwable throwable, Object... codeMessageArgs) {
        throw new UnsupportedOperationException(
            "The string message is not supported in BusinessLogicException. "
            + "Please use ErrorCode to specify the message."
        );
    }
}
