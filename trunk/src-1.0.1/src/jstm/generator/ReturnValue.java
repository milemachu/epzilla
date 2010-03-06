/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

/**
 * Return value of a method.
 */
public class ReturnValue extends ArgumentOrReturnValue {

    public ReturnValue() {
    }

    public ReturnValue(Type type) {
        super(type, null, null, false);
    }

    public ReturnValue(Class c) {
        this(new Type(c));
    }

    public ReturnValue(Structure structure) {
        this(new Type(structure));
    }

    public ReturnValue(Type type, boolean multiple) {
        super(type, null, null, multiple);
    }

    public ReturnValue(Class c, boolean multiple) {
        this(new Type(c), multiple);
    }

    public ReturnValue(Structure structure, boolean multiple) {
        this(new Type(structure), multiple);
    }

    public ReturnValue(Type type, String name, String description, boolean multiple) {
        super(type, name, description, multiple);
    }
}
