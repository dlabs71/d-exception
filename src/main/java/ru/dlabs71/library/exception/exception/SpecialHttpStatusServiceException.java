package ru.dlabs71.library.exception.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import ru.dlabs71.library.exception.type.ErrorCode;

/**
 * This class extends of {@link ServiceException} class. It is special exception implementation
 * with specific HTTP status.
 *
 * <p><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
@Getter
public final class SpecialHttpStatusServiceException extends ServiceException {

    private final HttpStatus httpStatus;

    public SpecialHttpStatusServiceException(
        String message,
        ErrorCode errorCode,
        @NonNull HttpStatus httpStatus
    ) {
        super(message, errorCode);
        this.httpStatus = httpStatus;
    }

    public SpecialHttpStatusServiceException(
        String message,
        ErrorCode errorCode,
        @NonNull Throwable cause,
        @NonNull HttpStatus httpStatus
    ) {
        super(message, errorCode, cause);
        this.httpStatus = httpStatus;
    }

    public static SpecialHttpStatusServiceException build(String message, HttpStatus httpStatus) {
        return new SpecialHttpStatusServiceException(message, null, httpStatus);
    }

    public static SpecialHttpStatusServiceException build(ErrorCode errorCode, HttpStatus httpStatus) {
        return new SpecialHttpStatusServiceException(null, errorCode, httpStatus);
    }

    public static SpecialHttpStatusServiceException build(
        String message,
        Throwable throwable,
        HttpStatus httpStatus
    ) {
        return new SpecialHttpStatusServiceException(message, null, throwable, httpStatus);
    }

    public static SpecialHttpStatusServiceException build(
        ErrorCode errorCode,
        Throwable throwable,
        HttpStatus httpStatus
    ) {
        return new SpecialHttpStatusServiceException(null, errorCode, throwable, httpStatus);
    }
}
