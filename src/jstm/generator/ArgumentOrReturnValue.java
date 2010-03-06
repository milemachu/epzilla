/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAttribute;

import jstm.misc.ImmutableClasses;

/**
 * Argument or return value of a method.
 */
public abstract class ArgumentOrReturnValue extends Data {

    /**
     * This method argument or return value will be generated as a List. Note
     * that lists generated using IsList are not transacted. It is different
     * than using TransactedList as the argument type. When you use IsList, you
     * can pass an ArrayList or any other non transacted list, and the lists
     * returned by method calls are simply a container to return multiple
     * values. All items will be sent individually, whereas if you use the
     * TransactedList type instead of IsList, the TransactedList itself is sent,
     * which can be more efficient if it is already present ion the remote site.
     */
    @XmlAttribute
    public boolean IsList;

    public ArgumentOrReturnValue() {
    }

    public ArgumentOrReturnValue(Type type, String name, String description, boolean isList) {
        super(type, name, description);

        IsList = isList;
    }

    public String getTypeName(Generator generator) {
        return getTypeName(generator, false);
    }

    public String getTypeName(Generator generator, boolean boxPrimitives) {
        Class c = Type.getNonGeneratedClass();
        String fullName = Type.getFullName(generator);

        if (boxPrimitives) {
            int classIndex = -1;

            if (c != null)
                classIndex = Arrays.asList(ImmutableClasses.Little).indexOf(c);

            if (classIndex != -1)
                fullName = ImmutableClasses.Big[classIndex].getSimpleName();
        }

        if (IsList) {
            if (generator.getTarget() == Generator.Target.Java5)
                return "List<" + fullName + ">";
            else
                return "List";
        }

        return fullName;
    }

    public ArrayList<String> getWriteStrings(String value) {
        ArrayList<String> list = new ArrayList<String>();

        if (IsList) {
            list.add("writer.writeInteger(((List) " + value + ").size());");
            list.add("for (java.util.Iterator iter = ((List) " + value + ").iterator(); iter.hasNext();)");
            list.add("    " + Type.getWriteString("iter.next()"));
        } else
            list.add(Type.getWriteString(value));

        return list;
    }

    public ArrayList<String> getReadStrings(Generator generator, String value) {
        ArrayList<String> list = new ArrayList<String>();

        if (IsList) {
            String length = "length";

            if (generator.getTarget() == Generator.Target.CSharp)
                length = "Length";

            list.add("{");
            list.add("    Object[] array = new Object[reader.readInteger()];");
            list.add("    for (int i = 0; i < array." + length + "; i++)");
            list.add("        array[i] = " + Type.getReadString());
            list.add("    " + value + " = java.util.Arrays.asList(array);");
            list.add("}");
        } else
            list.add(value + " = " + Type.getReadString());

        return list;
    }
}
