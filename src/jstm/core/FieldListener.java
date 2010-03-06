/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

/**
 * Called when a field changes on a transacted structure.
 */
public interface FieldListener {

    void onChange(Transaction transaction, int fieldIndex);
}
