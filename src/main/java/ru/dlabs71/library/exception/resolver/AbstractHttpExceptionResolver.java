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
import ru.dlabs71.library.exception.exception.SpecialHttpStatusServiceException;
import ru.dlabs71.library.exception.exception.WithoutStacktraceServiceException;
import ru.dlabs71.library.exception.type.CommonErrorCode;
import ru.dlabs71.library.exception.type.ErrorCode;
import ru.dlabs71.library.exception.util.ResponseEntityHelper;

/**
 * <p>
 * <div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractHttpExceptionResolver {

    @Getter
    protected final boolean enableStacktrace;
    protected final DExceptionMessageService messageService;
    protected final ResponseEntityHelper responseEntityHelper;

    protected AbstractHttpExceptionResolver(boolean enableStacktrace, DExceptionMessageService messageService) {
        this.enableStacktrace = enableStacktrace;
        this.messageService = messageService;
        this.responseEntityHelper = new ResponseEntityHelper(messageService);
    }

    protected ResponseEntity<ErrorResponseDto> resolveInformationException(
        HttpServletRequest request,
        BusinessLogicServiceException exception
    ) {
        logRequestException(request, exception);

        String description = responseEntityHelper.acquireMessage(exception);
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

        String description = responseEntityHelper.acquireMessage(exception);
        return new ResponseEntity<>(
            ErrorResponseDto.builder()
                .informative(false)
                .errorCode(exception.getErrorCode())
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

        String description = responseEntityHelper.acquireMessage(exception);
        return new ResponseEntity<>(
            ErrorResponseDto.builder()
                .informative(false)
                .errorCode(exception.getErrorCode())
                .message(description)
                .stacktrace(null)
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    protected ResponseEntity<ErrorResponseDto> resolveServiceException(
        HttpServletRequest request,
        SpecialHttpStatusServiceException exception
    ) {
        logRequestException(request, exception);

        String description = responseEntityHelper.acquireMessage(exception);
        return new ResponseEntity<>(
            ErrorResponseDto.builder()
                .informative(false)
                .errorCode(exception.getErrorCode())
                .message(description)
                .stacktrace(null)
                .build(),
            exception.getHttpStatus()
        );
    }

    protected ResponseEntity<ErrorResponseDto> resolveEntityNotFound(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse500(CommonErrorCode.ENTITY_NOT_FOUND, exception, enableStacktrace);
    }

    protected ResponseEntity<ErrorResponseDto> resolveOptimisticLock(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse500(CommonErrorCode.STALE_OBJECT, exception, enableStacktrace);
    }

    protected ResponseEntity<ErrorResponseDto> resolveLockException(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse500(CommonErrorCode.LOCK_OBJECT, exception, enableStacktrace);
    }

    protected ResponseEntity<ErrorResponseDto> resolveAccessDeniedException(
        HttpServletRequest request,
        Exception exception
    ) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse(
            CommonErrorCode.ACCESS_DENIED,
            HttpStatus.FORBIDDEN,
            exception,
            false
        );
    }

    protected ResponseEntity<ErrorResponseDto> resolveFileNotFoundException(
        HttpServletRequest request,
        Exception exception
    ) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse(
            CommonErrorCode.FILE_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            exception,
            false
        );
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
        return responseEntityHelper.makeResponse500(CommonErrorCode.COMMON_EXCEPTION, throwable, enableStacktrace);
    }

    protected ResponseEntity<ErrorResponseDto> resolveDefaultException(
        HttpServletRequest request,
        ErrorCode errorCode,
        Throwable throwable
    ) {
        logRequestException(request, throwable);
        return responseEntityHelper.makeResponse500(errorCode, throwable, enableStacktrace);
    }

    protected ResponseEntity<ErrorResponseDto> resolveDefaultException(
        HttpServletRequest request,
        ErrorCode errorCode,
        HttpStatus status,
        Throwable throwable,
        boolean withStacktrace
    ) {
        logRequestException(request, throwable);
        return responseEntityHelper.makeResponse(errorCode, status, throwable, withStacktrace);
    }

    protected void logRequestException(HttpServletRequest request, Throwable throwable) {
        log.debug("Unexpected exception processing request: {}", request.getRequestURI());
        log.error(String.format("Request exception: %s", throwable.getMessage()), throwable);
    }
}
