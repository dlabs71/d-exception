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
import ru.dlabs71.library.exception.utils.ResponseEntityHelper;

/**
 * Abstract class for implementing a standard exception resolver.
 *
 * <p>This class provides several helper methods to simplify the creation of exception handler methods.
 * These methods generate HTTP responses with appropriate status codes and response bodies.
 *
 * <p><div><strong>Project name:</strong> d-exception</div>
 * <div><strong>Creation date:</strong> 2024-08-24</div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
@Slf4j
public abstract class AbstractHttpExceptionResolver {

    /**
     * Indicates whether stack traces should be included in the HTTP response body.
     */
    @Getter
    protected final boolean enableStacktrace;

    /**
     * Service for converting error codes into human-readable messages.
     */
    protected final DExceptionMessageService messageService;

    /**
     * Helper class for creating {@link ResponseEntity} objects.
     */
    private final ResponseEntityHelper responseEntityHelper;

    /**
     * Constructs a new instance of the exception resolver.
     *
     * @param enableStacktrace Whether to include stack traces in the HTTP response body.
     * @param messageService   The service used to convert error codes into human-readable messages.
     */
    protected AbstractHttpExceptionResolver(boolean enableStacktrace, DExceptionMessageService messageService) {
        this.enableStacktrace = enableStacktrace;
        this.messageService = messageService;
        this.responseEntityHelper = new ResponseEntityHelper(messageService);
    }

    /**
     * Handles a business logic exception, typically including a detailed response body for the client.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The business logic exception to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: true</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveBusinessLogicException(
        HttpServletRequest request,
        BusinessLogicServiceException exception
    ) {
        logRequestException(request, exception);

        String message = responseEntityHelper.acquireMessage(exception);
        return new ResponseEntity<>(
            ErrorResponseDto.builder()
                .informative(true)
                .errorCode(exception.getErrorCode())
                .data(exception.getData())
                .level(exception.getLevel())
                .message(message)
                .stacktrace(enableStacktrace ? exception.getStackTrace() : null)
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Handles a generic service exception, typically including a response body for the client.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The service exception to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveServiceException(
        HttpServletRequest request,
        ServiceException exception
    ) {
        logRequestException(request, exception);

        String message = responseEntityHelper.acquireMessage(exception);
        return new ResponseEntity<>(
            ErrorResponseDto.builder()
                .informative(false)
                .errorCode(exception.getErrorCode())
                .message(message)
                .stacktrace(enableStacktrace ? exception.getStackTrace() : null)
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Handles a service exception without including a stack trace in the response.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The service exception to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveServiceException(
        HttpServletRequest request,
        WithoutStacktraceServiceException exception
    ) {
        logRequestException(request, exception);

        String message = responseEntityHelper.acquireMessage(exception);
        return new ResponseEntity<>(
            ErrorResponseDto.builder()
                .informative(false)
                .errorCode(exception.getErrorCode())
                .message(message)
                .stacktrace(null)
                .build(),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    /**
     * Handles a service exception with a custom HTTP status code.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The service exception to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: Custom status from the exception</li>
     *         <li>Informative: false</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveServiceException(
        HttpServletRequest request,
        SpecialHttpStatusServiceException exception
    ) {
        logRequestException(request, exception);

        String message = responseEntityHelper.acquireMessage(exception);
        return new ResponseEntity<>(
            ErrorResponseDto.builder()
                .informative(false)
                .errorCode(exception.getErrorCode())
                .message(message)
                .stacktrace(null)
                .build(),
            exception.getHttpStatus()
        );
    }

    /**
     * Handles an exception when an entity is not found or inaccessible to the user.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The exception to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#ENTITY_NOT_FOUND}</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveEntityNotFound(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse500(CommonErrorCode.ENTITY_NOT_FOUND, exception, enableStacktrace);
    }

    /**
     * Handles an exception when an entity is already modified by another user.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The exception to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#STALE_OBJECT}</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveOptimisticLock(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse500(CommonErrorCode.STALE_OBJECT, exception, enableStacktrace);
    }

    /**
     * Handles an exception when an entity is locked.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The exception to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#LOCK_OBJECT}</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveLockException(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse500(CommonErrorCode.LOCK_OBJECT, exception, enableStacktrace);
    }

    /**
     * Handles an exception when access to an entity is denied due to access policies.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The exception to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 403 (Forbidden)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#ACCESS_DENIED}</li>
     *     </ul>
     */
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

    /**
     * Handles an exception when a file is not found.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The exception to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 404 (Not Found)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#FILE_NOT_FOUND}</li>
     *     </ul>
     */
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

    /**
     * Handles a common IOException.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The IOException to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#IO_EXCEPTION}</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveIOException(
        HttpServletRequest request,
        IOException exception
    ) {
        return this.resolveDefaultException(request, CommonErrorCode.IO_EXCEPTION, exception);
    }

    /**
     * Handles a validation exception (e.g., assertion error).
     *
     * @param request The HTTP request that caused the exception.
     * @param error   The AssertionError to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#VALIDATION_EXCEPTION}</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveAssertationError(
        HttpServletRequest request,
        AssertionError error
    ) {
        return this.resolveDefaultException(request, CommonErrorCode.VALIDATION_EXCEPTION, error);
    }

    /**
     * Handles a generic exception.
     *
     * @param request   The HTTP request that caused the exception.
     * @param throwable The Throwable to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#COMMON_EXCEPTION} + message from the throwable</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveDefaultException(
        HttpServletRequest request,
        Throwable throwable
    ) {
        logRequestException(request, throwable);
        return responseEntityHelper.makeResponse500(CommonErrorCode.COMMON_EXCEPTION, throwable, enableStacktrace);
    }

    /**
     * Handles a generic exception with a specific error code.
     *
     * @param request   The HTTP request that caused the exception.
     * @param errorCode The specific error code to use.
     * @param throwable The Throwable to handle.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: From the error code</li>
     *     </ul>
     */
    protected ResponseEntity<ErrorResponseDto> resolveDefaultException(
        HttpServletRequest request,
        ErrorCode errorCode,
        Throwable throwable
    ) {
        logRequestException(request, throwable);
        return responseEntityHelper.makeResponse500(errorCode, throwable, enableStacktrace);
    }

    /**
     * Handles a generic exception with a specific error code and HTTP status.
     *
     * @param request        The HTTP request that caused the exception.
     * @param errorCode      The specific error code to use.
     * @param status         The specific HTTP status code to use.
     * @param throwable      The Throwable to handle.
     * @param withStacktrace Whether to include the stack trace in the response.
     *
     * @return A {@link ResponseEntity} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: From the status parameter</li>
     *         <li>Informative: false</li>
     *         <li>Message: From the error code</li>
     *     </ul>
     */
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

    /**
     * Logs the exception that occurred during the processing of an HTTP request.
     *
     * @param request   The HTTP request that caused the exception.
     * @param throwable The exception to log.
     */
    protected void logRequestException(HttpServletRequest request, Throwable throwable) {
        log.debug("d.Unexpected exception processing request: {}", request.getRequestURI());
        log.error(String.format("d.Request exception: %s", throwable.getMessage()), throwable);
    }
}