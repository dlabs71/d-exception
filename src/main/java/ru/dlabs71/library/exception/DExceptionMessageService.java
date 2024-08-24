package ru.dlabs71.library.exception;

public interface DExceptionMessageService {

    String getMessage(String code, Object... args);
}
