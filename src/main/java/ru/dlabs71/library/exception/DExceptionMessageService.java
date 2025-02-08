package ru.dlabs71.library.exception;

/**
 * Common interface for getting message by a code.
 */
public interface DExceptionMessageService {

    /**
     * Get message by a code.
     *
     * @param code a message code
     * @param args message arguments.
     *
     * @return created message text.
     */
    String getMessage(String code, Object... args);
}
