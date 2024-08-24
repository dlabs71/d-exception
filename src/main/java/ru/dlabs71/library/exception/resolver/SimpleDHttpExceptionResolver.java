package ru.dlabs71.library.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.dlabs71.library.exception.DExceptionMessageService;
import ru.dlabs71.library.exception.dto.ErrorResponseDto;
import ru.dlabs71.library.exception.exception.BusinessLogicServiceException;
import ru.dlabs71.library.exception.exception.ServiceException;
import ru.dlabs71.library.exception.exception.WithoutStacktraceServiceException;

/**
 * <p>
 * <div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@RestControllerAdvice
public class SimpleDHttpExceptionResolver extends AbstractHttpExceptionResolver {

    public SimpleDHttpExceptionResolver(boolean enableStacktrace, DExceptionMessageService messageService) {
        super(enableStacktrace, messageService);
    }

    @Override
    @ExceptionHandler({ BusinessLogicServiceException.class })
    public ResponseEntity<ErrorResponseDto> resolveInformationException(
        HttpServletRequest request,
        BusinessLogicServiceException exception
    ) {
        return super.resolveInformationException(request, exception);
    }

    @Override
    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<ErrorResponseDto> resolveServiceException(
        HttpServletRequest request,
        ServiceException exception
    ) {
        return super.resolveServiceException(request, exception);
    }

    @Override
    @ExceptionHandler({ WithoutStacktraceServiceException.class })
    public ResponseEntity<ErrorResponseDto> resolveServiceException(
        HttpServletRequest request,
        WithoutStacktraceServiceException exception
    ) {
        return super.resolveServiceException(request, exception);
    }

    @ExceptionHandler({ FileNotFoundException.class })
    public ResponseEntity<ErrorResponseDto> resolveFileNotFoundException(
        HttpServletRequest request,
        Exception exception
    ) {
        return super.resolveNotFoundException(request, exception);
    }

    @Override
    @ExceptionHandler({ IOException.class })
    public ResponseEntity<ErrorResponseDto> resolveIOException(
        HttpServletRequest request,
        IOException exception
    ) {
        return super.resolveIOException(request, exception);
    }

    @Override
    @ExceptionHandler({ AssertionError.class })
    protected ResponseEntity<ErrorResponseDto> resolveAssertationError(
        HttpServletRequest request,
        AssertionError error
    ) {
        return super.resolveAssertationError(request, error);
    }

    @Override
    @ExceptionHandler({ Throwable.class })
    public ResponseEntity<ErrorResponseDto> resolveDefaultException(HttpServletRequest request, Throwable exception) {
        return super.resolveDefaultException(request, exception);
    }
}
