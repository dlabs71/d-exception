package ru.dlabs71.library.exception.exception;

import lombok.NonNull;
import ru.dlabs71.library.exception.type.ErrorCode;

/**
 * This class extends of {@link ServiceException} class. It is a special exception implementation
 * who guarantees won't set up stacktrace into a response body.
 *
 * <p><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
public final class WithoutStacktraceServiceException extends ServiceException {

    public WithoutStacktraceServiceException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public WithoutStacktraceServiceException(String message, ErrorCode errorCode, @NonNull Throwable cause) {
        super(message, errorCode, cause);
    }

    public static WithoutStacktraceServiceException build(String message) {
        return new WithoutStacktraceServiceException(message, null);
    }

    public static WithoutStacktraceServiceException build(ErrorCode errorCode) {
        return new WithoutStacktraceServiceException(null, errorCode);
    }

    public static WithoutStacktraceServiceException build(String message, Throwable throwable) {
        return new WithoutStacktraceServiceException(message, null, throwable);
    }

    public static WithoutStacktraceServiceException build(ErrorCode errorCode, Throwable throwable) {
        return new WithoutStacktraceServiceException(null, errorCode, throwable);
    }
}
