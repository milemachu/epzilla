/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

public interface MethodCallback<T> {

    void onResult(T result);

    void onTransactionAborted();

    void onException(String message);
}
