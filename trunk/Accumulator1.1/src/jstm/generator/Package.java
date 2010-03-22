/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

public class Package {

    @XmlAttribute
    public String Name;

    @XmlElementWrapper(name = "Packages")
    @XmlElement(name = "Package")
    public ArrayList<Package> Packages = new ArrayList<Package>();

    @XmlElementWrapper(name = "Structures")
    @XmlElement(name = "Structure")
    public ArrayList<Structure> Structures = new ArrayList<Structure>();

    @XmlTransient
    protected Package _package;

    public Package() {
    }

    public Package(String name) {
        Name = name;
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
