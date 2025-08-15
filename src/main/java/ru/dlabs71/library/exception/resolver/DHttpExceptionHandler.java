package ru.dlabs71.library.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import ru.dlabs71.library.exception.DExceptionMessageService;
import ru.dlabs71.library.exception.dto.ErrorResponseDto;
import ru.dlabs71.library.exception.exception.BusinessLogicException;
import ru.dlabs71.library.exception.exception.ServiceException;
import ru.dlabs71.library.exception.exception.SpecialHttpStatusServiceException;
import ru.dlabs71.library.exception.exception.WithoutStacktraceServiceException;
import ru.dlabs71.library.exception.type.CommonErrorCode;
import ru.dlabs71.library.exception.type.DHttpStatus;
import ru.dlabs71.library.exception.type.ErrorCode;
import ru.dlabs71.library.exception.utils.ResponseEntityHelper;

/**
 * A utility class for constructing the HTTP response body from an exception.
 *
 * <p>This class provides several helper methods to simplify the creation of exception handler methods.
 * These methods generate HTTP responses with appropriate status codes and response bodies.
 *
 * <p>Utilize this class in your HTTP exception resolver following the composition tenet.
 *
 * <p><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2025-02-25 </div>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Slf4j
public final class DHttpExceptionHandler {


    /**
     * Indicates whether stack traces should be included in the HTTP response body.
     */
    @Getter
    private final boolean enableStacktrace;

    /**
     * Service for converting error codes into human-readable messages.
     */
    @Getter
    private final DExceptionMessageService messageService;

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
    public DHttpExceptionHandler(boolean enableStacktrace, DExceptionMessageService messageService) {
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
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: true</li>
     *     </ul>
     */
    public DHttpResponse resolveBusinessLogicException(
        HttpServletRequest request, BusinessLogicException exception
    ) {
        logRequestException(request, exception);

        String message = responseEntityHelper.acquireMessage(exception);
        return new DHttpResponse(DHttpStatus.INTERNAL_SERVER_ERROR.getValue(), ErrorResponseDto.builder()
            .informative(true)
            .errorCode(exception.getErrorCode())
            .data(exception.getData())
            .level(exception.getLevel())
            .message(message)
            .stacktrace(enableStacktrace ? exception.getStackTrace() : null)
            .build());
    }

    /**
     * Handles a generic service exception, typically including a response body for the client.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The service exception to handle.
     *
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *     </ul>
     */
    public DHttpResponse resolveServiceException(
        HttpServletRequest request, ServiceException exception
    ) {
        logRequestException(request, exception);

        String message = responseEntityHelper.acquireMessage(exception);
        return new DHttpResponse(DHttpStatus.INTERNAL_SERVER_ERROR.getValue(), ErrorResponseDto.builder()
            .informative(false)
            .errorCode(exception.getErrorCode())
            .message(message)
            .stacktrace(enableStacktrace ? exception.getStackTrace() : null)
            .build());
    }

    /**
     * Handles a service exception without including a stack trace in the response.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The service exception to handle.
     *
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *     </ul>
     */
    public DHttpResponse resolveServiceException(
        HttpServletRequest request, WithoutStacktraceServiceException exception
    ) {
        logRequestException(request, exception);

        String message = responseEntityHelper.acquireMessage(exception);
        return new DHttpResponse(DHttpStatus.INTERNAL_SERVER_ERROR.getValue(), ErrorResponseDto.builder().informative(
            false).errorCode(exception.getErrorCode()).message(message).stacktrace(null).build());
    }

    /**
     * Handles a service exception with a custom HTTP status code.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The service exception to handle.
     *
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: Custom status from the exception</li>
     *         <li>Informative: false</li>
     *     </ul>
     */
    public DHttpResponse resolveServiceException(
        HttpServletRequest request, SpecialHttpStatusServiceException exception
    ) {
        logRequestException(request, exception);

        String message = responseEntityHelper.acquireMessage(exception);
        return new DHttpResponse(
            exception.getHttpStatus(),
            ErrorResponseDto.builder()
                .informative(false)
                .errorCode(exception.getErrorCode())
                .message(message)
                .stacktrace(null)
                .build()
        );
    }

    /**
     * Handles an exception when an entity is not found or inaccessible to the user.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The exception to handle.
     *
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#ENTITY_NOT_FOUND}</li>
     *     </ul>
     */
    public DHttpResponse resolveEntityNotFound(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.buildResponse500(CommonErrorCode.ENTITY_NOT_FOUND, exception, enableStacktrace);
    }

    /**
     * Handles an exception when an entity is already modified by another user.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The exception to handle.
     *
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#STALE_OBJECT}</li>
     *     </ul>
     */
    public DHttpResponse resolveOptimisticLock(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.buildResponse500(CommonErrorCode.STALE_OBJECT, exception, enableStacktrace);
    }

    /**
     * Handles an exception when an entity is locked.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The exception to handle.
     *
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#LOCK_OBJECT}</li>
     *     </ul>
     */
    public DHttpResponse resolveLockException(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.buildResponse500(CommonErrorCode.LOCK_OBJECT, exception, enableStacktrace);
    }

    /**
     * Handles an exception when access to an entity is denied due to access policies.
     *
     * @param request   The HTTP request that caused the exception.
     * @param exception The exception to handle.
     *
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 403 (Forbidden)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#ACCESS_DENIED}</li>
     *     </ul>
     */
    public DHttpResponse resolveAccessDeniedException(
        HttpServletRequest request, Exception exception
    ) {
        logRequestException(request, exception);
        return responseEntityHelper.buildResponse(
            CommonErrorCode.ACCESS_DENIED,
            DHttpStatus.FORBIDDEN.getValue(),
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
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 404 (Not Found)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#FILE_NOT_FOUND}</li>
     *     </ul>
     */
    public DHttpResponse resolveFileNotFoundException(
        HttpServletRequest request, Exception exception
    ) {
        logRequestException(request, exception);
        return responseEntityHelper.buildResponse(
            CommonErrorCode.FILE_NOT_FOUND,
            DHttpStatus.NOT_FOUND.getValue(),
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
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#IO_EXCEPTION}</li>
     *     </ul>
     */
    public DHttpResponse resolveIOException(
        HttpServletRequest request, IOException exception
    ) {
        return this.resolveDefaultException(request, CommonErrorCode.IO_EXCEPTION, exception);
    }

    /**
     * Handles a validation exception (e.g., assertion error).
     *
     * @param request The HTTP request that caused the exception.
     * @param error   The AssertionError to handle.
     *
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#VALIDATION_EXCEPTION}</li>
     *     </ul>
     */
    public DHttpResponse resolveAssertationError(
        HttpServletRequest request, AssertionError error
    ) {
        return this.resolveDefaultException(request, CommonErrorCode.VALIDATION_EXCEPTION, error);
    }

    /**
     * Handles a generic exception.
     *
     * @param request   The HTTP request that caused the exception.
     * @param throwable The Throwable to handle.
     *
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: {@link CommonErrorCode#COMMON_EXCEPTION} + message from the throwable</li>
     *     </ul>
     */
    public DHttpResponse resolveDefaultException(
        HttpServletRequest request, Throwable throwable
    ) {
        logRequestException(request, throwable);
        return responseEntityHelper.buildResponse500(CommonErrorCode.COMMON_EXCEPTION, throwable, enableStacktrace);
    }

    /**
     * Handles a generic exception with a specific error code.
     *
     * @param request   The HTTP request that caused the exception.
     * @param errorCode The specific error code to use.
     * @param throwable The Throwable to handle.
     *
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: 500 (Internal Server Error)</li>
     *         <li>Informative: false</li>
     *         <li>Message: From the error code</li>
     *     </ul>
     */
    public DHttpResponse resolveDefaultException(
        HttpServletRequest request, ErrorCode errorCode, Throwable throwable
    ) {
        logRequestException(request, throwable);
        return responseEntityHelper.buildResponse500(errorCode, throwable, enableStacktrace);
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
     * @return A {@link DHttpResponse} containing an {@link ErrorResponseDto} as the response body.
     *     <ul>
     *         <li>HTTP status: From the status parameter</li>
     *         <li>Informative: false</li>
     *         <li>Message: From the error code</li>
     *     </ul>
     */
    public DHttpResponse resolveDefaultException(
        HttpServletRequest request, ErrorCode errorCode, int status, Throwable throwable, boolean withStacktrace
    ) {
        logRequestException(request, throwable);
        return responseEntityHelper.buildResponse(errorCode, status, throwable, withStacktrace);
    }

    /**
     * Logs the exception that occurred during the processing of an HTTP request.
     *
     * @param request   The HTTP request that caused the exception.
     * @param throwable The exception to log.
     */
    private void logRequestException(HttpServletRequest request, Throwable throwable) {
        log.debug("Unexpected exception processing request: {}", request.getRequestURI());
        log.error(String.format("Request exception: %s", throwable.getMessage()), throwable);
    }
}
