package ru.dlabs71.library.exception.exception;

import lombok.NonNull;
import ru.dlabs71.library.exception.type.ErrorCode;

public class WithoutStacktraceServiceException extends ServiceException {

    public WithoutStacktraceServiceException(String description, ErrorCode errorCode) {
        super(description, errorCode);
    }

    public WithoutStacktraceServiceException(String description, ErrorCode errorCode, @NonNull Throwable cause) {
        super(description, errorCode, cause);
    }
}
