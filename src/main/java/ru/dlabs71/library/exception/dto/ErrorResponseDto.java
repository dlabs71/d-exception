package ru.dlabs71.library.exception.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.dlabs71.library.exception.type.ErrorCode;
import ru.dlabs71.library.exception.type.ErrorLevel;

/**
 * <p>
 * <div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorResponseDto implements Serializable {

    /**
     * This is indicator of informative message.
     * Usually it is a business logic exception {@see BusinessLogicServiceException}.
     * It points to client to display a UI alert or popup.
     **/
    private boolean informative;

    /**
     * It is complement code for client. It is optional.
     */
    private ErrorCode errorCode;

    /**
     * It is level (or type) for a client alert popup.
     */
    private ErrorLevel level;

    /**
     * It is error message.
     */
    private String message;

    /**
     * It is error stacktrace of an exception.
     */
    private StackTraceElement[] stacktrace;

    /**
     * It is complement data object for client. You can set here any extra data for client.
     */
    private Object data;
}
