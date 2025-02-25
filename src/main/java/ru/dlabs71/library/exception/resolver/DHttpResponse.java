package ru.dlabs71.library.exception.resolver;

import ru.dlabs71.library.exception.dto.ErrorResponseDto;

/**
 * DTO for the methods of {@link DHttpExceptionHandler}, representing an HTTP response.
 *
 * <p><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2025-02-25 </div>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public record DHttpResponse(int httpStatus, ErrorResponseDto body) {

}
