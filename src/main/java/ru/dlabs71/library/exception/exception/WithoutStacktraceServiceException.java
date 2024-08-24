package ru.dlabs71.library.exception.exception;

import lombok.NonNull;
import ru.dlabs71.library.exception.type.ErrorCode;

/**
 * <p>
 * <div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public class WithoutStacktraceServiceException extends ServiceException {

    public WithoutStacktraceServiceException(String description, ErrorCode errorCode) {
        super(description, errorCode);
    }

    public WithoutStacktraceServiceException(String description, ErrorCode errorCode, @NonNull Throwable cause) {
        super(description, errorCode, cause);
    }

    public static WithoutStacktraceServiceException build(String description) {
        return new WithoutStacktraceServiceException(description, null);
    }

    public static WithoutStacktraceServiceException build(ErrorCode errorCode) {
        return new WithoutStacktraceServiceException(null, errorCode);
    }

    public static WithoutStacktraceServiceException build(String description, Throwable throwable) {
        return new WithoutStacktraceServiceException(description, null, throwable);
    }

    public static WithoutStacktraceServiceException build(ErrorCode errorCode, Throwable throwable) {
        return new WithoutStacktraceServiceException(null, errorCode, throwable);
    }
}
