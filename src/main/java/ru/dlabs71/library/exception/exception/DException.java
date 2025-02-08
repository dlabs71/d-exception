package ru.dlabs71.library.exception.exception;

import ru.dlabs71.library.exception.type.ErrorCode;

/**
 * Common interface for exception classes. All your exceptions must implement this.
 * <p>
 * <div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface DException {

    String getMessage();

    ErrorCode getErrorCode();

    String getCauseExceptionMessage();
}
