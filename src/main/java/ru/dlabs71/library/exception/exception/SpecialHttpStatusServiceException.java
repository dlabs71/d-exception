package ru.dlabs71.library.exception.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import ru.dlabs71.library.exception.type.ErrorCode;

/**
 * This class extends of {@link ServiceException} class. It is special exception implementation
 * with specific HTTP status.
 *
 * <br><br><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
@Getter
public final class SpecialHttpStatusServiceException extends ServiceException {

    private final int httpStatus;

    public SpecialHttpStatusServiceException(
        String message,
        ErrorCode errorCode,
        @NonNull HttpStatus httpStatus,
        Object... codeMessageArgs
    ) {
        super(message, errorCode, codeMessageArgs);
        this.httpStatus = httpStatus.value();
    }

    public SpecialHttpStatusServiceException(
        String message,
        ErrorCode errorCode,
        @NonNull Throwable cause,
        @NonNull HttpStatus httpStatus,
        Object... codeMessageArgs
    ) {
        super(message, errorCode, cause, codeMessageArgs);
        this.httpStatus = httpStatus.value();
    }

    public SpecialHttpStatusServiceException(
        String message,
        ErrorCode errorCode,
        int httpStatus,
        Object... codeMessageArgs
    ) {
        super(message, errorCode, codeMessageArgs);
        this.httpStatus = httpStatus;
    }

    public SpecialHttpStatusServiceException(
        String message,
        ErrorCode errorCode,
        @NonNull Throwable cause,
        int httpStatus,
        Object... codeMessageArgs
    ) {
        super(message, errorCode, cause, codeMessageArgs);
        this.httpStatus = httpStatus;
    }

    public static SpecialHttpStatusServiceException build(
        String message,
        HttpStatus httpStatus,
        Object... codeMessageArgs
    ) {
        return new SpecialHttpStatusServiceException(message, null, httpStatus, codeMessageArgs);
    }

    public static SpecialHttpStatusServiceException build(
        ErrorCode errorCode,
        HttpStatus httpStatus,
        Object... codeMessageArgs
    ) {
        return new SpecialHttpStatusServiceException(null, errorCode, httpStatus, codeMessageArgs);
    }

    public static SpecialHttpStatusServiceException build(
        String message,
        Throwable throwable,
        HttpStatus httpStatus,
        Object... codeMessageArgs
    ) {
        return new SpecialHttpStatusServiceException(message, null, throwable, httpStatus, codeMessageArgs);
    }

    public static SpecialHttpStatusServiceException build(
        ErrorCode errorCode,
        Throwable throwable,
        HttpStatus httpStatus,
        Object... codeMessageArgs
    ) {
        return new SpecialHttpStatusServiceException(null, errorCode, throwable, httpStatus, codeMessageArgs);
    }
}
