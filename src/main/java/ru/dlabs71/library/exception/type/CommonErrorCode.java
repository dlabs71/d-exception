package ru.dlabs71.library.exception.type;

import lombok.Getter;

/**
 * Error codes which is supplied the library.
 *
 * <p><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
@Getter
public enum CommonErrorCode implements ErrorCode {
    COMMON_EXCEPTION("d.common.exception"),
    ENTITY_NOT_FOUND("d.entity.not.found.exception"),
    STALE_OBJECT("d.stale.object.exception"),
    LOCK_OBJECT("d.lock.object.exception"),
    ACCESS_DENIED("d.access.denied.exception"),
    INVALID_REQUEST("d.invalid.request.exception"),
    SECURITY_EXCEPTION("d.security.exception"),
    SERVICE_NOT_FOUND("d.error.module.not.available"),
    VALIDATION_EXCEPTION("d.validation.exception"),
    RESOURCE_NOT_FOUND("d.resource.not.found.exception"),
    FILE_NOT_FOUND("d.file.not.found.exception"),
    IO_EXCEPTION("d.io.exception");

    private final String codeMessage;

    CommonErrorCode(String codeMessage) {
        this.codeMessage = codeMessage;
    }

}
