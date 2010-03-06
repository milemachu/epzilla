/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Field of a transacted structure.
 */
public class Field extends Data {

    /**
     * When adding a structure to a share, all referenced objects are also
     * added, unless the field is marked as transient. You can also modify this
     * behavior by changing the ReferencesWalkingMode of the share.
     */
    @XmlAttribute
    public boolean Transient;

    public Field() {
    }

    public Field(Class type, String name) {
        this(new Type(type), name, null, false);
    }

    public Field(Structure structure, String name) {
        this(new Type(structure), name, null, false);
    }

    public Field(Type type, String name) {
        this(type, name, null, false);
    }

    public Field(Type type, String name, String description, boolean transient_) {
        super(type, name, description);

        Transient = transient_;
    }
}
