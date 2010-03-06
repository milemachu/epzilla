/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

import java.util.ArrayList;

import javax.xml.bind.annotation.*;

import jstm.core.TransactedObject;
import jstm.misc.Debug;
import jstm.misc.ImmutableClasses;

public class Type {

    @XmlAttribute
    public String GeneratedClass;

    @XmlElementWrapper(name = "GenericsParameters")
    @XmlElement(name = "GenericsParameter")
    public ArrayList<Type> GenericsParameters = new ArrayList<Type>();

    private Class _class;

    private Structure _structure;

    public Type() {
    }

    public Type(Class c) {
        _class = c;
    }

    public Type(Structure structure) {
        _structure = structure;
    }

    @XmlAttribute
    public String getName() {
        if (_class != null)
            return _class.getName();

        return null;
    }

    public void setName(String value) {
        _class = findClass(value);
    }

    private static Class findClass(String name) {
        for (Class little : ImmutableClasses.Little)
            if (little.getSimpleName().equals(name))
                return little;

        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Only supported classes should be unmarshalled");
        }
    }

    protected void prepare() {
        if (_class != null && !(TransactedObject.class.isAssignableFrom(_class)))
            if (!ImmutableClasses.supported(_class))
                throw new IllegalArgumentException("Class " + _class + " is not yet supported.");

        if (_structure != null && GeneratedClass == null)
            GeneratedClass = _structure.getFullName();

        if (GenericsParameters != null) {
            if (GenericsParameters.size() == 0)
                GenericsParameters = null;
            else
                for (Type type : GenericsParameters)
                    type.prepare();
        }
    }

    public void addGenericsParameter(Class c) {
        addGenericsParameter(new Type(c));
    }

    public void addGenericsParameter(Structure structure) {
        addGenericsParameter(new Type(structure));
    }

    public void addGenericsParameter(Type type) {
        if (GenericsParameters == null)
            GenericsParameters = new ArrayList<Type>();

        GenericsParameters.add(type);
    }

    @XmlTransient
    public Class getNonGeneratedClass() {
        return _class;
    }

    public String getFullName(Generator generator) {
        Debug.assertion((_class == null) != (GeneratedClass == null));

        if (GeneratedClass != null)
            return GeneratedClass;
        else {
            String s = _class.getSimpleName();

            if (generator.getTarget() == Generator.Target.Java5) {
                if (GenericsParameters != null && GenericsParameters.size() > 0) {
                    String params = "";

                    for (Type type : GenericsParameters) {
                        if (params.length() != 0)
                            params += ", ";

                        params += type.getFullName(generator);
                    }

                    s += "<" + params + ">";
                }
            }

            return s;
        }
    }

    public String getWriteString(String value) {
        if (_class == null || TransactedObject.class.isAssignableFrom(_class))
            return "writer.writeTransactedObject((TransactedObject) " + value + ");";
        else {
            int index = ImmutableClasses.getIndex(_class);

            if (index != -1) {
                Class little = ImmutableClasses.Little[index];
                Class big = ImmutableClasses.Big[index];

                String line = "writer.write" + big.getSimpleName() + "(";
                line += "((" + big.getName() + ") " + value + ")." + little.getSimpleName() + "Value()";
                line += ");";
                return line;
            }

            String name = _class.getSimpleName();
            return "writer.write" + name + "((" + name + ") " + value + ");";

        }
    }

    @XmlTransient
    public String getReadString() {
        if (_class == null || TransactedObject.class.isAssignableFrom(_class))
            return "reader.readTransactedObject();";
        else {
            int index = ImmutableClasses.getIndex(_class);

            if (index != -1) {
                Class big = ImmutableClasses.Big[index];
                String line = "new " + big.getName() + "(";
                line += "reader.read" + big.getSimpleName() + "());";
                return line;
            }

            return "reader.read" + _class.getSimpleName() + "();";
        }
    }
}
