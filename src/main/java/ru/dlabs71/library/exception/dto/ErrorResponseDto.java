package ru.dlabs71.library.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.dlabs71.library.exception.type.ErrorCode;
import ru.dlabs71.library.exception.type.ErrorLevel;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponseDto {

    private boolean informative;
    private ErrorCode errorCode;
    private ErrorLevel level;
    private String message;
    private StackTraceElement[] stacktrace;
    private Object data;

    public static ErrorResponseDtoBuilder builder(ErrorCode errorCode, String message, Throwable cause) {
        return new ErrorResponseDtoBuilder()
            .errorCode(errorCode)
            .message(message)
            .stacktrace(cause.getStackTrace())
            .informative(false);
    }

    public static ErrorResponseDtoBuilder builder(ErrorCode errorCode, String message) {
        return new ErrorResponseDtoBuilder()
            .errorCode(errorCode)
            .message(message)
            .informative(false);
    }

    public static ErrorResponseDtoBuilder builder(String message, Throwable cause) {
        return new ErrorResponseDtoBuilder()
            .message(message)
            .stacktrace(cause.getStackTrace())
            .informative(false);
    }

    public static ErrorResponseDtoBuilder builder(String message) {
        return new ErrorResponseDtoBuilder()
            .message(message)
            .informative(false);
    }

    public static ErrorResponseDtoBuilder builder() {
        return new ErrorResponseDtoBuilder();
    }
}
