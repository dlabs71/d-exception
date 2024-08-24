package ru.dlabs71.library.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.dlabs71.library.exception.DExceptionMessageService;
import ru.dlabs71.library.exception.dto.ErrorResponseDto;
import ru.dlabs71.library.exception.exception.BusinessLogicServiceException;
import ru.dlabs71.library.exception.exception.ServiceException;
import ru.dlabs71.library.exception.exception.WithoutStacktraceServiceException;
import ru.dlabs71.library.exception.type.CommonErrorCode;
import ru.dlabs71.library.exception.type.ErrorCode;


@Slf4j
public abstract class AbstractHttpExceptionResolver {

    @Getter
    protected final boolean enableStacktrace;
    protected final DExceptionMessageService messageService;
    protected final ErrorResponseServant errorResponseServant;

    protected AbstractHttpExceptionResolver(boolean enableStacktrace, DExceptionMessageService messageService) {
        this.enableStacktrace = enableStacktrace;
        this.messageService = messageService;
        this.errorResponseServant = new ErrorResponseServant(messageService);
    }

    protected ResponseEntity<ErrorResponseDto> resolveInformationException(
        HttpServletRequest request,
        BusinessLogicServiceException exception
    ) {
        logRequestException(request, exception);

        String description = errorResponseServant.acquireMessage(exception);
        return new ResponseEntity<>(
            ErrorResponseDto.builder()
                .informative(true)
                .errorCode(exception.getErrorCode())
                .data(exception.getData())
                .level(exception.getLevel())
                .message(description)
                .stacktrace(enableStacktrace ? exception.getStackTrace() : null)
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    protected ResponseEntity<ErrorResponseDto> resolveServiceException(
        HttpServletRequest request,
        ServiceException exception
    ) {
        logRequestException(request, exception);

        String description = errorResponseServant.acquireMessage(exception);
        return new ResponseEntity<>(
            ErrorResponseDto.builder()
                .informative(false)
                .errorCode(exception.getErrorCode())
                .data(null)
                .message(description)
                .stacktrace(enableStacktrace ? exception.getStackTrace() : null)
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    protected ResponseEntity<ErrorResponseDto> resolveServiceException(
        HttpServletRequest request,
        WithoutStacktraceServiceException exception
    ) {
        logRequestException(request, exception);

        String description = errorResponseServant.acquireMessage(exception);
        return new ResponseEntity<>(
            ErrorResponseDto.builder()
                .informative(false)
                .errorCode(exception.getErrorCode())
                .data(null)
                .message(description)
                .stacktrace(null)
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    protected ResponseEntity<ErrorResponseDto> resolveEntityNotFound(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return errorResponseServant.makeResponse(CommonErrorCode.ENTITY_NOT_FOUND, exception);
    }

    protected ResponseEntity<ErrorResponseDto> resolveOptimisticLock(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return errorResponseServant.makeResponse(CommonErrorCode.STALE_OBJECT, exception);
    }

    protected ResponseEntity<ErrorResponseDto> resolveLockException(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return errorResponseServant.makeResponse(CommonErrorCode.LOCK_OBJECT, exception);
    }

    protected ResponseEntity<ErrorResponseDto> resolveAccessDeniedException(
        HttpServletRequest request,
        Exception exception
    ) {
        logRequestException(request, exception);
        return errorResponseServant.makeResponse(CommonErrorCode.ACCESS_DENIED, HttpStatus.FORBIDDEN, exception, true);
    }

    protected ResponseEntity<ErrorResponseDto> resolveNotFoundException(
        HttpServletRequest request,
        Exception exception
    ) {
        logRequestException(request, exception);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity<ErrorResponseDto> resolveIOException(
        HttpServletRequest request,
        IOException exception
    ) {
        return this.resolveDefaultException(request, CommonErrorCode.IO_EXCEPTION, exception);
    }

    protected ResponseEntity<ErrorResponseDto> resolveAssertationError(
        HttpServletRequest request,
        AssertionError error
    ) {
        return this.resolveDefaultException(request, CommonErrorCode.VALIDATION_EXCEPTION, error);
    }

    protected ResponseEntity<ErrorResponseDto> resolveDefaultException(
        HttpServletRequest request,
        Throwable throwable
    ) {
        logRequestException(request, throwable);
        return errorResponseServant.makeResponse(CommonErrorCode.UNEXPECTED, throwable);
    }

    protected ResponseEntity<ErrorResponseDto> resolveDefaultException(
        HttpServletRequest request,
        ErrorCode errorCode,
        Throwable throwable
    ) {
        logRequestException(request, throwable);
        return errorResponseServant.makeResponse(errorCode, throwable);
    }

    protected ResponseEntity<ErrorResponseDto> resolveDefaultException(
        HttpServletRequest request,
        ErrorCode errorCode,
        HttpStatus status,
        Throwable throwable,
        boolean withoutStacktrace
    ) {
        logRequestException(request, throwable);
        return errorResponseServant.makeResponse(errorCode, status, throwable, withoutStacktrace);
    }

    protected void logRequestException(HttpServletRequest request, Throwable throwable) {
        log.debug("Unexpected exception processing request: {}", request.getRequestURI());
        log.error(String.format("Request exception: %s", throwable.getMessage()), throwable);
    }
}
