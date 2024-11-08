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
 * Abstract class for implementation standard exception resolver. This class define several helper methods for
 * easy to make exception handler methods. These methods create http response with a properly http status code and body.
 * See {@link SimpleHttpExceptionResolver} for an example.
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
    private final ResponseEntityHelper responseEntityHelper;

    /**
     * Constructor of the class.
     *
     * @param enableStacktrace on/off specifying stacktrace in a http response body.
     * @param messageService   implementation of the {@link DExceptionMessageService} for
     *                         convert a code message to a normal message.
     */
    protected AbstractHttpExceptionResolver(boolean enableStacktrace, DExceptionMessageService messageService) {
        this.enableStacktrace = enableStacktrace;
        this.messageService = messageService;
        this.responseEntityHelper = new ResponseEntityHelper(messageService);
    }

    /**
     * Handle a business logic exception. Usually include full-fledged response body for a client.
     *
     * @param request   a http user request
     * @param exception implementation of the Exception class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
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
     * Handle a business logic exception. Usually include full-fledged response body for a client.
     *
     * @param request   a http user request
     * @param exception implementation of the Exception class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
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
     * Handle an exception when object/entity not found or not accessed for a user.
     *
     * @param request   a http user request
     * @param exception implementation of the Exception class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
     */
    protected ResponseEntity<ErrorResponseDto> resolveEntityNotFound(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse500(CommonErrorCode.ENTITY_NOT_FOUND, exception, enableStacktrace);
    }

    /**
     * Handle an exception when object/entity is already changed another user.
     *
     * @param request   a http user request
     * @param exception implementation of the Exception class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
     */
    protected ResponseEntity<ErrorResponseDto> resolveOptimisticLock(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse500(CommonErrorCode.STALE_OBJECT, exception, enableStacktrace);
    }

    /**
     * Handle an exception when object/entity is locked.
     *
     * @param request   a http user request
     * @param exception implementation of the Exception class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
     */
    protected ResponseEntity<ErrorResponseDto> resolveLockException(HttpServletRequest request, Exception exception) {
        logRequestException(request, exception);
        return responseEntityHelper.makeResponse500(CommonErrorCode.LOCK_OBJECT, exception, enableStacktrace);
    }

    /**
     * Handle an exception when object/entity is inaccessible for a user by your access policies.
     *
     * @param request   a http user request
     * @param exception implementation of the Exception class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
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
     * Handle an exception when a file is not found.
     *
     * @param request   a http user request
     * @param exception implementation of the Exception class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
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
     * Handle a common IOException.
     *
     * @param request   a http user request
     * @param exception implementation of the Exception class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
     */
    protected ResponseEntity<ErrorResponseDto> resolveIOException(
        HttpServletRequest request,
        IOException exception
    ) {
        return this.resolveDefaultException(request, CommonErrorCode.IO_EXCEPTION, exception);
    }

    /**
     * Handle a validation exception (assert keyword).
     *
     * @param request a http user request
     * @param error   implementation of the AssertionError class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
     */
    protected ResponseEntity<ErrorResponseDto> resolveAssertationError(
        HttpServletRequest request,
        AssertionError error
    ) {
        return this.resolveDefaultException(request, CommonErrorCode.VALIDATION_EXCEPTION, error);
    }

    /**
     * Handle a common exception.
     *
     * @param request   a http user request
     * @param throwable implementation of the Throwable class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
     */
    protected ResponseEntity<ErrorResponseDto> resolveDefaultException(
        HttpServletRequest request,
        Throwable throwable
    ) {
        logRequestException(request, throwable);
        return responseEntityHelper.makeResponse500(CommonErrorCode.COMMON_EXCEPTION, throwable, enableStacktrace);
    }

    /**
     * Handle a common exception.
     *
     * @param request   a http user request
     * @param errorCode a specific error code. See documentation for {@link ErrorCode}
     * @param throwable implementation of the Throwable class
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
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
     * Handle a common exception.
     *
     * @param request        a http user request
     * @param errorCode      a specific error code. See documentation for {@link ErrorCode}
     * @param status         a specific HTTP status code.
     * @param throwable      implementation of the Throwable class
     * @param withStacktrace on/off including stacktrace in a response body
     *
     * @return ResponseEntity with filled ErrorResponseDto class as body of a response
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

    protected void logRequestException(HttpServletRequest request, Throwable throwable) {
        log.debug("Unexpected exception processing request: {}", request.getRequestURI());
        log.error(String.format("Request exception: %s", throwable.getMessage()), throwable);
    }
}
