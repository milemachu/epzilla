/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.core;

import jstm.misc.Debug;

final class MethodCall {

    public final TransactedStructure Structure;

    public final int Index;

    public final Object[] Arguments;

    public Object Result;

    public MethodCallback _callback;

    public MethodCall(TransactedStructure structure, int index, Object[] arguments) {
        Structure = structure;
        Index = index;
        Arguments = arguments;
    }

    protected static class Error {

        public final String Message;

        public Error(String message) {
            Message = message;
        }
    }

    protected void run() {
        try {
            Result = Structure.call(Index, Arguments);
        } catch (Throwable t) {
            Debug.log(0, "Exception on method call on " + Structure + ": " + t);
            Result = new MethodCall.Error(t.toString());
        }
    }
}
