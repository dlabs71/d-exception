package ru.dlabs71.library.exception.type;

import lombok.Getter;

/**
 * Helper enum with the http statuses and their codes.
 *
 * <br><br><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2025-02-25 </div>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
@Getter
public enum DHttpStatus {

    INTERNAL_SERVER_ERROR(500),
    NOT_FOUND(404),
    FORBIDDEN(403);

    private final int value;

    DHttpStatus(int value) {
        this.value = value;
    }
}
