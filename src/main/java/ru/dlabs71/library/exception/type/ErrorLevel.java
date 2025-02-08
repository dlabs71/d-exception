package ru.dlabs71.library.exception.type;

import java.io.Serializable;

/**
 * Error level is code in the body of a http response that it is indicator for a client
 * must handle response as: ERROR, WARNING, INFO, etc. You can create your own implementation class or enum.
 *
 * <p><div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 *
 * @author Ivanov Danila
 * @since 0.0.1
 */
public interface ErrorLevel extends Serializable {

    String name();
}
