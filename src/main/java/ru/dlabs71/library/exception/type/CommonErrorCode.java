package ru.dlabs71.library.exception.type;

import lombok.Getter;

/**
 * Error codes which is supplied the library.
 * <p>
 * <div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Getter
public enum CommonErrorCode implements ErrorCode {
    COMMON_EXCEPTION("common.exception"),
    ENTITY_NOT_FOUND("entity.not.found.exception"),
    STALE_OBJECT("stale.object.exception"),
    LOCK_OBJECT("lock.object.exception"),
    ACCESS_DENIED("access.denied.exception"),
    INVALID_REQUEST("invalid.request.exception"),
    SECURITY_EXCEPTION("security.exception"),
    SERVICE_NOT_FOUND("error.module.not.available"),
    VALIDATION_EXCEPTION("validation.exception"),
    RESOURCE_NOT_FOUND("resource.not.found.exception"),
    FILE_NOT_FOUND("file.not.found.exception"),
    IO_EXCEPTION("io.exception");

    private final String codeMessage;

    CommonErrorCode(String codeMessage) {
        this.codeMessage = codeMessage;
    }

}
