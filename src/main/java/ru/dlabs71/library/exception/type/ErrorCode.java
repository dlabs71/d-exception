package ru.dlabs71.library.exception.type;

import java.io.Serializable;

/**
 * Error code is entity for describe an exception in type-save manner. It contains code of text message and name.
 * <p>
 * <div><strong>Project name:</strong> d-exception </div>
 * <div><strong>Creation date:</strong> 2024-08-24 </div>
 * </p>
 *
 * @author Ivanov Danila
 * @since 1.0.0
 */
public interface ErrorCode extends Serializable {

    String getCodeMessage();

    String name();
}
