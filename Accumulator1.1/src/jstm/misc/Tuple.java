/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.misc;

public class Tuple<A, B> {

    public final A A;

    public final B B;

    public Tuple(A a, B b) {
        A = a;
        B = b;
    }
}
