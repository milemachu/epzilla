/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator.files;

import java.util.ArrayList;
import java.util.Arrays;

import jstm.core.TransactedObject;
import jstm.generator.*;
import jstm.misc.ImmutableClasses;

public abstract class StructureWriter extends FileGenerator {

    protected final Generator _generator;

    protected final Structure _structure;

    protected StructureWriter(Generator generator, Structure structure, String folder) {
        super(folder, structure.getPackage().getFullName(), structure.Name);

        _generator = generator;
        _structure = structure;
    }

    protected String finalString() {
        return "final";
    }

    protected String overrideString() {
        return " ";
    }

    protected String throwsString() {
        return "throws java.io.IOException ";
    }

    protected String booleanString() {
        return "boolean";
    }

    protected final String getBigType(Field field) {
        int little = -1;

        if (field.Type.getNonGeneratedClass() != null)
            little = Arrays.asList(ImmutableClasses.Little).indexOf(field.Type.getNonGeneratedClass());

        if (little != -1)
            return ImmutableClasses.Big[little].getName();

        return null;
    }

    protected final void writeFieldGet(int index) {
        Field field = _structure.Fields.get(index);
        String big = getBigType(field);

        if (big != null) {
            wl("        " + big + " value = (" + big + ") getField(" + index + ");");
            wl();
            wl("        if(value != null)");
            wl("            return value." + field.Type.getNonGeneratedClass().getSimpleName() + "Value();");
            wl();

            if (field.Type.getNonGeneratedClass() == boolean.class)
                wl("        return false;");
            else
                wl("        return 0;");
        } else
            wl("        return (" + field.Type.getFullName(_generator) + ") getField(" + index + ");");
    }

    protected void writeField(int index) {
        Field field = _structure.Fields.get(index);

        // Getter
        {
            if (field.Description != null && field.Description.length() > 0)
                wl("    /** " + field.Description + " */");

            if (getJava5() && field.Type.getFullName(_generator).contains("<"))
                wl("    @SuppressWarnings(\"unchecked\")");

            wl("    public " + field.Type.getFullName(_generator) + " get" + field.Name + "() {");

            writeFieldGet(index);

            wl("    }");
        }

        wl();

        // Setter
        {
            if (field.Description != null && field.Description.length() > 0)
                wl("    /** " + field.Description + " */");

            wl("    public void set" + field.Name + "(" + field.Type.getFullName(_generator) + " value) {");

            String big = getBigType(field);

            if (big != null)
                wl("        setField(" + index + ", new " + big + "(value));");
            else
                wl("        setField(" + index + ", value);");

            wl("    }");
        }

        wl();
    }

    protected boolean getJava5() {
        return _generator.getTarget() == jstm.generator.Generator.Target.Java5;
    }

    protected boolean getCSharp() {
        return _generator.getTarget() == jstm.generator.Generator.Target.CSharp;
    }

    protected abstract void writeSerialization();

    protected abstract void writeArgumentsSerialization();

    protected abstract void writeResultsSerialization();

    @Override
    protected void body() {
        wl();

        for (int i = 0; i < _structure.Fields.size(); i++)
            writeField(i);

        //

        for (int i = 0; i < _structure.Fields.size(); i++) {
            String upName = _structure.Fields.get(i).Name.toUpperCase();

            wl("    public static " + finalString() + " int " + upName + "_INDEX = " + i + ";");
            wl();

            String name = "\"" + _structure.Fields.get(i).Name + "\"";

            wl("    public static " + finalString() + " String " + upName + "_NAME = " + name + ";");
            wl();
        }

        wl("    public static final int FIELD_COUNT = " + _structure.Fields.size() + ";");
        wl();

        if (getJava5())
            wl("    @Override");

        wl("    public" + overrideString() + "String getFieldName(int index) {");
        wl("        return getFieldNameStatic(index);");
        wl("    }");
        wl();
        wl("    public static String getFieldNameStatic(int index) {");
        wl("        switch (index) {");

        for (int i = 0; i < _structure.Fields.size(); i++) {
            wl("            case " + i + ":");
            wl("                return \"" + _structure.Fields.get(i).Name + "\";");
        }

        wl("            default:");
        wl("                throw new java.lang.IllegalArgumentException();");
        wl("        }");
        wl("    }");
        wl();

        for (Method method : _structure.Methods) {
            MethodWriter writer = new MethodWriter(this, method);
            writer.writeCalls();
            wl();
        }

        wl("    // Internal");
        wl();

        ArrayList<Integer> indexes = new ArrayList<Integer>();

        for (int i = 0; i < _structure.Fields.size(); i++) {
            Class c = _structure.Fields.get(i).Type.getNonGeneratedClass();

            if (c == null || TransactedObject.class.isAssignableFrom(c))
                if (!_structure.Fields.get(i).Transient)
                    indexes.add(i);
        }

        String s = "";

        for (int i : indexes) {
            if (s.length() != 0)
                s += ", ";

            s += i;
        }

        if (getJava5())
            wl("    @Override");

        wl("    protected" + overrideString() + "int getClassId() {");
        wl("        return " + _generator.getObjectModelDefinition().getAllStructures().indexOf(_structure) + ";");
        wl("    }");
        wl();

        if (getJava5())
            wl("    @Override");

        wl("    public" + overrideString() + "String getObjectModelUID() {");
        wl("        return \"" + _generator.getUID() + "\";");
        wl("    }");
        wl();

        wl("    private static " + finalString() + " int[] NON_TRANSIENT_FIELDS = new int[] { " + s + " };");
        wl();

        if (getJava5())
            wl("    @Override");

        wl("    protected" + overrideString() + "int[] getNonTransientFields() {");
        wl("        return NON_TRANSIENT_FIELDS;");
        wl("    }");
        wl();

        writeSerialization();

        if (_structure.Methods.size() > 0) {
            wl();
            wl("    // Methods");
            wl();

            if (getJava5())
                wl("    @Override");

            wl("    protected" + overrideString() + "Object call(int index, Object[] args) {");
            wl("        switch (index) {");

            for (int i = 0; i < _structure.Methods.size(); i++) {
                MethodWriter writer = new MethodWriter(this, _structure.Methods.get(i));

                wl("            case " + i + ":");

                String invoke = writer.getInvokeString();
                wl("                " + invoke);

                if (!invoke.startsWith("return"))
                    wl("                return null;");
            }

            wl("            default:");
            wl("                throw new java.lang.IllegalStateException();");
            wl("        }");
            wl("    }");
            wl();

            writeArgumentsSerialization();

            wl();

            writeResultsSerialization();
        }

        wl("}");
    }
}
