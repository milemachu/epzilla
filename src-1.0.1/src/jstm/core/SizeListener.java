/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

/**
 * Can be registered on transacted collections or maps to get notified when
 * their size changes.
 */
public interface SizeListener {

    void onResize(Transaction transaction, int oldSize, int newSize);
}
