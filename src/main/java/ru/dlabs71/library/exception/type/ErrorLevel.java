package ru.dlabs71.library.exception.type;

/**
 * Error level is code in the body of a http response that it is indicator for a client
 * must handle response as: ERROR, WARNING, INFO, etc. You can create your own implementation class or enum.
 * <p>
 * <div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface ErrorLevel {

    String name();
}
