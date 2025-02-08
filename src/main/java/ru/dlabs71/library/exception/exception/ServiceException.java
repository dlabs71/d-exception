package ru.dlabs71.library.exception.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.dlabs71.library.exception.type.ErrorCode;

/**
 * Base exception class implemented {@linkplain DException} interface.
 * It is usually ancestor for user-defined exception classes. This exception contains a normal text message
 * and error code. When message is not passed value the message will retrieve from errorCode (if it is specified).
 *
 * <p>{@link BusinessLogicServiceException}, {@link SpecialHttpStatusServiceException},
 * {@link WithoutStacktraceServiceException}
 *
 * <p><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
@Getter
@Setter
public class ServiceException extends RuntimeException implements DException {

    private String message;
    private ErrorCode errorCode;

    /**
     * Constructor of the class.
     *
     * @param message   a message explain cause of an exception.
     * @param errorCode special error code. It can be replacement for the message
     *                  or an extra info field in an HTTP response body for client.
     */
    public ServiceException(String message, ErrorCode errorCode) {
        super(message);
        if (message == null && errorCode == null) {
            throw new IllegalArgumentException("d.Message and ErrorCode are both null");
        }
        this.errorCode = errorCode;
        this.message = message;
    }

    /**
     * Constructor of the class.
     *
     * @param message   a message explain cause of an exception. If it starts with '$' then the method
     *                  tries to get message from the messageService by a code.
     *                  <br>For example:
     *                  <br>If message = "Hello" then output is "Hello"
     *                  <br>If message = "$code.greeting" then output is message by the code "code.greeting"
     * @param errorCode special error code. It can be replacement for the message
     *                  or an extra info field in an HTTP response body for client.
     * @param cause     a throwable object - cause of exception
     */
    public ServiceException(String message, ErrorCode errorCode, @NonNull Throwable cause) {
        super(cause.getMessage(), cause);
        if (message == null && errorCode == null) {
            throw new IllegalArgumentException("d.Message and ErrorCode are both null");
        }
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getCauseExceptionMessage() {
        return this.getMessage();
    }

    public static ServiceException build(String message) {
        return new ServiceException(message, null);
    }

    public static ServiceException build(ErrorCode errorCode) {
        return new ServiceException(null, errorCode);
    }

    public static ServiceException build(String message, Throwable throwable) {
        return new ServiceException(message, null, throwable);
    }

    public static ServiceException build(ErrorCode errorCode, Throwable throwable) {
        return new ServiceException(null, errorCode, throwable);
    }
}
