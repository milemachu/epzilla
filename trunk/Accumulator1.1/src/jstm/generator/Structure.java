/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

public class Structure {

    @XmlAttribute
    public String Name;

    public Structure Parent;

    @XmlElementWrapper(name = "Fields")
    @XmlElement(name = "Field")
    public ArrayList<Field> Fields = new ArrayList<Field>();

    @XmlElementWrapper(name = "Methods")
    @XmlElement(name = "Method")
    public ArrayList<Method> Methods = new ArrayList<Method>();

    @XmlTransient
    protected Package _package;

    public Structure() {
    }

    public Structure(String name) {
        this(name, null);
    }

    public Structure(String name, Structure parent) {
        Name = name;
        Parent = parent;
    }

    @XmlTransient
    public Package getPackage() {
        return _package;
    }

    @XmlTransient
    public String getFullName() {
        String s = "";

        if (_package != null)
            s += _package.getFullName() + ".";

        return s + Name;
    }
}
