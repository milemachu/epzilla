/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

/**
 * Logs all transactions related events.
 */
public class Logger implements Site.InternalListener {

    public Logger() {
    }

    public void onCommitting(Transaction transaction) {
        if (transaction.getPrivateObjects() != null) {
            for (TransactedObject.Version version : transaction.getPrivateObjects().values())
                System.out.println(transaction + ": " + version.toString());
        }
    }

    public void onCommitted(Transaction transaction, long newVersion) {
    }

    public void onAborted(Transaction transaction) {
    }
}
