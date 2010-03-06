/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Base for structure fields and method arguments and return values.
 */
public abstract class Data {

    public Type Type;

    @XmlAttribute
    public String Name;

    @XmlAttribute
    public String Description;

    public Data() {
    }

    public Data(Type type, String name, String description) {
        if (type == null)
            throw new IllegalArgumentException();

        Type = type;
        Name = name;
        Description = description;
    }
}
