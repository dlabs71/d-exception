package ru.dlabs71.library.exception.utils;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import ru.dlabs71.library.exception.DExceptionMessageService;
import ru.dlabs71.library.exception.dto.ErrorResponseDto;
import ru.dlabs71.library.exception.exception.DException;
import ru.dlabs71.library.exception.resolver.DHttpResponse;
import ru.dlabs71.library.exception.type.CommonErrorCode;
import ru.dlabs71.library.exception.type.DHttpStatus;
import ru.dlabs71.library.exception.type.ErrorCode;

/**
 * Helper class for creating HTTP response.
 *
 * <br><br><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
@RequiredArgsConstructor
public final class ResponseEntityHelper {

    private final DExceptionMessageService messageService;

    public DHttpResponse buildResponse500(
        ErrorCode errorCode,
        Throwable cause,
        boolean withStacktrace
    ) {
        return this.buildResponse(null, errorCode, DHttpStatus.INTERNAL_SERVER_ERROR.getValue(), cause, withStacktrace);
    }

    public DHttpResponse buildResponse500(
        String message,
        Throwable cause,
        boolean withStacktrace
    ) {
        return this.buildResponse(message, null, DHttpStatus.INTERNAL_SERVER_ERROR.getValue(), cause, withStacktrace);
    }

    public DHttpResponse buildResponse(
        ErrorCode errorCode,
        int status,
        Throwable cause,
        boolean withStacktrace
    ) {
        return this.buildResponse(null, errorCode, status, cause, withStacktrace);
    }

    public DHttpResponse buildResponse(
        String message,
        int status,
        Throwable cause,
        boolean withStacktrace
    ) {
        return this.buildResponse(message, null, status, cause, withStacktrace);
    }

    /**
     * Create response entity using parameters.
     *
     * @param message        a message explain cause of an exception.
     * @param errorCode      special error code. It can be replacement for message
     *                       or an extra info field in an HTTP response body for client.
     * @param status         an HTTP status
     * @param cause          a throwable object - cause of exception
     * @param withStacktrace if it's true, the stacktrace from a throwable
     *                       will be assigned to the field {@linkplain ErrorResponseDto#stacktrace}
     *
     * @return a prepared DHttpResponse object
     */
    public DHttpResponse buildResponse(
        String message,
        ErrorCode errorCode,
        int status,
        Throwable cause,
        boolean withStacktrace
    ) {
        String acquiredMessage = this.acquireMessage(message, errorCode, cause.getMessage());
        ErrorResponseDto dto;
        if (withStacktrace) {
            dto = ErrorResponseDto.builder()
                .errorCode(errorCode)
                .message(acquiredMessage)
                .stacktrace(cause.getStackTrace())
                .build();
        } else {
            dto = ErrorResponseDto.builder()
                .errorCode(errorCode)
                .message(acquiredMessage)
                .build();
        }
        return new DHttpResponse(status, dto);
    }

    /**
     * Acquire message for the text message parameter by an exception.
     *
     * @param exception instance of the {@link DException}
     *
     * @return string message for {@link ErrorResponseDto}.
     */
    public String acquireMessage(DException exception) {
        return this.acquireMessage(
            exception.getMessage(),
            exception.getErrorCode(),
            exception.getCauseExceptionMessage(),
            exception.getCodeMessageArgs()
        );
    }

    /**
     * Acquire message base on the parameters.
     *
     * @param message          a message explain cause of an exception. If it starts with '$' then the method
     *                         tries to get message from the messageService by a code.
     *                         <br>For example:
     *                         <br>If message = "Hello" then output is "Hello"
     *                         <br>If message = "$code.greeting" then output is message by the code "code.greeting"
     * @param errorCode        error code. If the message isn't passed then message will be acquired by
     *                         the code associated with a value of the error code.
     * @param exceptionMessage message from cause exception
     * @param codeMessageArgs  parameters for substitution in the message template from the error code
     *                         or message parameters.
     *
     * @return The message will be determined in the following order:
     *     1) If the errorCode is specified, the message will be retrieved using the message code.
     *     2) If the errorCode is not specified but a message is provided,
     *     the message will be taken from the message parameter.
     *     3) If neither errorCode nor message is specified, the message will be taken
     *     from the exceptionMessage parameter.
     */
    public String acquireMessage(
        String message,
        ErrorCode errorCode,
        String exceptionMessage,
        Object... codeMessageArgs
    ) {
        if (message == null || message.isEmpty()) {
            return this.getMessage(
                Objects.requireNonNullElse(errorCode, CommonErrorCode.COMMON_EXCEPTION),
                exceptionMessage,
                codeMessageArgs
            );
        } else {
            if (message.startsWith("$")) {
                return messageService.getMessage(
                    message.substring(1),
                    exceptionMessage
                );
            }
        }
        return message;
    }

    private String getMessage(ErrorCode errorCode, String exceptionMessage, Object... codeMessageArgs) {
        if (codeMessageArgs != null && codeMessageArgs.length > 0) {
            Object[] args = new Object[codeMessageArgs.length + 1];
            args[codeMessageArgs.length] = exceptionMessage;
            System.arraycopy(codeMessageArgs, 0, args, 0, codeMessageArgs.length);
            return messageService.getMessage(errorCode.getCodeMessage(), args);
        }
        return messageService.getMessage(
            errorCode.getCodeMessage(),
            exceptionMessage
        );
    }
}
