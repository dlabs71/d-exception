package ru.dlabs71.library.exception.exception;

import lombok.NonNull;
import ru.dlabs71.library.exception.type.ErrorCode;

/**
 * This class extends of {@link ServiceException} class. It is a special exception implementation
 * who guarantees won't set up stacktrace into a response body.
 *
 * <br><br><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
public final class WithoutStacktraceServiceException extends ServiceException {

    public WithoutStacktraceServiceException(String message, ErrorCode errorCode, Object... codeMessageArgs) {
        super(message, errorCode, codeMessageArgs);
    }

    public WithoutStacktraceServiceException(
        String message,
        ErrorCode errorCode,
        @NonNull Throwable cause,
        Object... codeMessageArgs
    ) {
        super(message, errorCode, cause, codeMessageArgs);
    }

    public static WithoutStacktraceServiceException build(String message, Object... codeMessageArgs) {
        return new WithoutStacktraceServiceException(message, null, codeMessageArgs);
    }

    public static WithoutStacktraceServiceException build(ErrorCode errorCode, Object... codeMessageArgs) {
        return new WithoutStacktraceServiceException(null, errorCode, codeMessageArgs);
    }

    public static WithoutStacktraceServiceException build(
        String message,
        Throwable throwable,
        Object... codeMessageArgs
    ) {
        return new WithoutStacktraceServiceException(message, null, throwable, codeMessageArgs);
    }

    public static WithoutStacktraceServiceException build(
        ErrorCode errorCode,
        Throwable throwable,
        Object... codeMessageArgs
    ) {
        return new WithoutStacktraceServiceException(null, errorCode, throwable, codeMessageArgs);
    }
}
