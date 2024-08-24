package ru.dlabs71.library.exception.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
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
@Setter
public class ServiceException extends RuntimeException implements DException {

    private String description;
    private ErrorCode errorCode;

    public ServiceException(String description, ErrorCode errorCode) {
        super(description);
        this.errorCode = errorCode;
        this.description = description;
    }

    public ServiceException(String description, ErrorCode errorCode, @NonNull Throwable cause) {
        super(cause.getMessage(), cause);
        this.errorCode = errorCode;
        this.description = description;
    }

    @Override
    public String getCauseExceptionMessage() {
        return this.getMessage();
    }

    public static ServiceException build(String description) {
        return new ServiceException(description, null);
    }

    public static ServiceException build(ErrorCode errorCode) {
        return new ServiceException(null, errorCode);
    }

    public static ServiceException build(String description, Throwable throwable) {
        return new ServiceException(description, null, throwable);
    }

    public static ServiceException build(ErrorCode errorCode, Throwable throwable) {
        return new ServiceException(null, errorCode, throwable);
    }
}
