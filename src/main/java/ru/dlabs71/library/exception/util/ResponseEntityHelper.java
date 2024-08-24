package ru.dlabs71.library.exception.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.dlabs71.library.exception.DExceptionMessageService;
import ru.dlabs71.library.exception.dto.ErrorResponseDto;
import ru.dlabs71.library.exception.exception.DException;
import ru.dlabs71.library.exception.type.CommonErrorCode;
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
@RequiredArgsConstructor
public class ResponseEntityHelper {

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

    public ResponseEntity<ErrorResponseDto> makeResponse(
        String description,
        ErrorCode errorCode,
        HttpStatus status,
        Throwable cause,
        boolean withStacktrace
    ) {
        String message = this.acquireMessage(description, errorCode, cause.getMessage());
        ErrorResponseDto dto;
        if (withStacktrace) {
            dto = ErrorResponseDto.builder()
                .errorCode(errorCode)
                .message(message)
                .stacktrace(cause.getStackTrace())
                .build();
        } else {
            dto = ErrorResponseDto.builder()
                .errorCode(errorCode)
                .message(message)
                .build();
        }
        return new ResponseEntity<>(dto, status);
    }

    public String acquireMessage(DException exception) {
        return this.acquireMessage(
            exception.getDescription(),
            exception.getErrorCode(),
            exception.getCauseExceptionMessage()
        );
    }

    public String acquireMessage(String description, ErrorCode errorCode, String exceptionMessage) {
        if (description == null || description.isEmpty()) {
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
            if (description.startsWith("$")) {
                return messageService.getMessage(
                    description.substring(1),
                    exceptionMessage
                );
            }
        }
        return description;
    }
}
