package ru.dlabs71.library.exception.exception;

import ru.dlabs71.library.exception.type.ErrorCode;

/**
 * Common interface for the exception classes in project d-exception.
 * <p>
 * <div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface DException {

    String getDescription();

    ErrorCode getErrorCode();

    String getCauseExceptionMessage();
}
