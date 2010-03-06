/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Method {

    @XmlAttribute
    public String Name;

    @XmlElementWrapper(name = "Arguments")
    @XmlElement(name = "Argument")
    public ArrayList<Argument> Arguments = new ArrayList<Argument>();

    public ReturnValue ReturnValue = new ReturnValue(new Type(void.class), null, null, false);

    public Method() {
    }

    public Method(String name) {
        Name = name;
    }

    public boolean hasList() {
        for (Argument arg : Arguments)
            if (arg.IsList)
                return true;

        if (ReturnValue.IsList)
            return true;

        return false;
    }
}
