/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

/**
 * Argument of a method.
 */
public class Argument extends ArgumentOrReturnValue {

    public Argument() {
    }

    public Argument(Class c, String name) {
        super(new Type(c), name, null, false);
    }

    public Argument(Structure structure, String name) {
        super(new Type(structure), name, null, false);
    }

    public Argument(Type type, String name, String description, boolean multiple) {
        super(type, name, description, multiple);
    }
}
