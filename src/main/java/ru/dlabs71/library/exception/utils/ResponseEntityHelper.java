package ru.dlabs71.library.exception.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.dlabs71.library.exception.DExceptionMessageService;
import ru.dlabs71.library.exception.dto.ErrorResponseDto;
import ru.dlabs71.library.exception.exception.DException;
import ru.dlabs71.library.exception.type.CommonErrorCode;
import ru.dlabs71.library.exception.type.ErrorCode;

/**
 * Helper class for creating HTTP response.
 *
 * <p><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
@RequiredArgsConstructor
public final class ResponseEntityHelper {

    private final DExceptionMessageService messageService;

    public ResponseEntity<ErrorResponseDto> makeResponse500(
        ErrorCode errorCode,
        Throwable cause,
        boolean withStacktrace
    ) {
        return this.makeResponse(null, errorCode, HttpStatus.INTERNAL_SERVER_ERROR, cause, withStacktrace);
    }

    public ResponseEntity<ErrorResponseDto> makeResponse(
        ErrorCode errorCode,
        HttpStatus status,
        Throwable cause,
        boolean withStacktrace
    ) {
        return this.makeResponse(null, errorCode, status, cause, withStacktrace);
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
     * @return a prepared ResponseEntity object
     */
    public ResponseEntity<ErrorResponseDto> makeResponse(
        String message,
        ErrorCode errorCode,
        HttpStatus status,
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
        return new ResponseEntity<>(dto, status);
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
            exception.getCauseExceptionMessage()
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
     *
     * @return if the message is null or empty then message will be acquired by the error code.
     *     If error code is null then message will be equal with the exceptionMessage.
     */
    public String acquireMessage(String message, ErrorCode errorCode, String exceptionMessage) {
        if (message == null || message.isEmpty()) {
            if (errorCode == null) {
                return messageService.getMessage(
                    CommonErrorCode.COMMON_EXCEPTION.getCodeMessage(),
                    exceptionMessage
                );
            } else {
                return messageService.getMessage(
                    errorCode.getCodeMessage(),
                    exceptionMessage
                );
            }
        } else {
            if (message.startsWith("d.$")) {
                return messageService.getMessage(
                    message.substring(1),
                    exceptionMessage
                );
            }
        }
        return message;
    }
}
