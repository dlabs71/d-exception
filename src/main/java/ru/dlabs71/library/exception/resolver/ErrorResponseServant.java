package ru.dlabs71.library.exception.resolver;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.dlabs71.library.exception.DExceptionMessageService;
import ru.dlabs71.library.exception.dto.ErrorResponseDto;
import ru.dlabs71.library.exception.exception.DException;
import ru.dlabs71.library.exception.type.CommonErrorCode;
import ru.dlabs71.library.exception.type.ErrorCode;

@RequiredArgsConstructor
public class ErrorResponseServant {

    private final DExceptionMessageService messageService;

    public ResponseEntity<ErrorResponseDto> makeResponse(String description, Throwable cause) {
        return this.makeResponse(description, null, HttpStatus.INTERNAL_SERVER_ERROR, cause, false);
    }

    public ResponseEntity<ErrorResponseDto> makeResponse(ErrorCode errorCode, Throwable cause) {
        return this.makeResponse(null, errorCode, HttpStatus.INTERNAL_SERVER_ERROR, cause, false);
    }

    public ResponseEntity<ErrorResponseDto> makeResponse(Throwable cause) {
        return this.makeResponse(null, CommonErrorCode.UNEXPECTED, HttpStatus.INTERNAL_SERVER_ERROR, cause, false);
    }

    public ResponseEntity<ErrorResponseDto> makeResponseWithoutStacktrace(Throwable cause) {
        return this.makeResponse(null, CommonErrorCode.UNEXPECTED, HttpStatus.INTERNAL_SERVER_ERROR, cause, true);
    }

    public ResponseEntity<ErrorResponseDto> makeResponse(
        ErrorCode errorCode,
        HttpStatus status,
        Throwable cause,
        boolean withoutStacktrace
    ) {
        return this.makeResponse(null, errorCode, status, cause, withoutStacktrace);
    }

    public ResponseEntity<ErrorResponseDto> makeResponse(
        String description,
        ErrorCode errorCode,
        HttpStatus status,
        Throwable cause,
        boolean withoutStacktrace
    ) {
        String message = this.acquireMessage(description, errorCode, cause.getMessage());
        ErrorResponseDto dto;
        if (withoutStacktrace) {
            dto = ErrorResponseDto.builder(errorCode, message).build();
        } else {
            dto = ErrorResponseDto.builder(errorCode, message, cause).build();
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
                    CommonErrorCode.COMMON.getCodeMessage(),
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
