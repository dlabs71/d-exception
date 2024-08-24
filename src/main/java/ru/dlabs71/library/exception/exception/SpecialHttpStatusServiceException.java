package ru.dlabs71.library.exception.exception;

import lombok.Getter;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
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
@Getter
public class SpecialHttpStatusServiceException extends ServiceException {

    private final HttpStatus httpStatus;

    public SpecialHttpStatusServiceException(
        String description,
        ErrorCode errorCode,
        @NonNull HttpStatus httpStatus
    ) {
        super(description, errorCode);
        this.httpStatus = httpStatus;
    }

    public SpecialHttpStatusServiceException(
        String description,
        ErrorCode errorCode,
        @NonNull Throwable cause,
        @NonNull HttpStatus httpStatus
    ) {
        super(description, errorCode, cause);
        this.httpStatus = httpStatus;
    }

    public static SpecialHttpStatusServiceException build(String description, HttpStatus httpStatus) {
        return new SpecialHttpStatusServiceException(description, null, httpStatus);
    }

    public static SpecialHttpStatusServiceException build(ErrorCode errorCode, HttpStatus httpStatus) {
        return new SpecialHttpStatusServiceException(null, errorCode, httpStatus);
    }

    public static SpecialHttpStatusServiceException build(
        String description,
        Throwable throwable,
        HttpStatus httpStatus
    ) {
        return new SpecialHttpStatusServiceException(description, null, throwable, httpStatus);
    }

    public static SpecialHttpStatusServiceException build(
        ErrorCode errorCode,
        Throwable throwable,
        HttpStatus httpStatus
    ) {
        return new SpecialHttpStatusServiceException(null, errorCode, throwable, httpStatus);
    }
}
