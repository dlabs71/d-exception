package ru.dlabs71.library.exception.type;

import lombok.Getter;

@Getter
public enum CommonErrorCode implements ErrorCode {
    COMMON("common.exception"),
    ENTITY_NOT_FOUND("entity.not.found.exception"),
    STALE_OBJECT("stale.object.exception"),
    LOCK_OBJECT("lock.object.exception"),
    UNEXPECTED("unexpected.exception"),
    ACCESS_DENIED("access.denied.exception"),
    INVALID_REQUEST("invalid.request.exception"),
    SECURITY_EXCEPTION("security.exception"),
    SERVICE_NOT_FOUND("error.module.not.available"),
    VALIDATION_EXCEPTION("validation.exception"),
    NOT_FOUND("validation.exception"),
    FILE_NOT_FOUND("validation.exception"),
    IO_EXCEPTION("validation.exception");


    private final String codeMessage;

    CommonErrorCode(String codeMessage) {
        this.codeMessage = codeMessage;
    }

}
