/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator.files;

import java.util.ArrayList;

import jstm.generator.Generator;
import jstm.generator.Method;
import jstm.generator.Structure;

public class MethodWriter {

    protected final Generator _generator;

    private final StructureWriter _writer;

    private final Structure _structure;

    private final Method _method;

    protected MethodWriter(StructureWriter writer, Method method) {
        _generator = writer._generator;
        _writer = writer;
        _structure = writer._structure;
        _method = method;
    }

    protected void writeCalls() {
        String args = "";
        String names = "";

        for (int i = 0; i < _method.Arguments.size(); i++) {
            String name = _method.Arguments.get(i).Name;
            String arg = _method.Arguments.get(i).getTypeName(_generator) + " " + name;

            if (args.length() == 0) {
                args += arg;
                names += name;
            } else {
                args += ", " + arg;
                names += ", " + name;
            }
        }

        String callback = "MethodCallback";

        if (_generator.getTarget() == Generator.Target.Java5) {
            Class c = _method.ReturnValue.Type.getNonGeneratedClass();

            if (c == void.class)
                callback += "<Object>";
            else {
                callback += "<" + _method.ReturnValue.getTypeName(_generator, true) + ">";
            }
        }

        int index = _structure.Methods.indexOf(_method);

        wl("    public void " + _method.Name + "(" + args + ", " + callback + " callback) {");
        wl("        addCall(" + index + ", new Object[] { " + names + " }, callback);");
        wl("    }");
        wl();
        wl("    protected " + _method.ReturnValue.getTypeName(_generator) + " " + _method.Name + "(" + args + ") {");
        wl("        String message = \"You must override this method in a derived class, \";");
        wl("        message += \"then put an instance of the derived class in the share.\";");
        wl("        throw new java.lang.IllegalStateException(message);");
        wl("    }");
    }

    protected String getInvokeString() {
        String args = "";

        for (int i = 0; i < _method.Arguments.size(); i++) {
            String arg = "(" + _method.Arguments.get(i).Type.getFullName(_generator) + ") args[" + i + "]";

            if (args.length() == 0)
                args += arg;
            else
                args += ", " + arg;
        }

        if (_method.ReturnValue.Type.getNonGeneratedClass() == void.class)
            return _method.Name + "(" + args + ");";
        else
            return "return " + _method.Name + "(" + args + ");";
    }

    protected ArrayList<String> getSerializeArgumentsStrings() {
        ArrayList<String> lines = new ArrayList<String>();

        for (int i = 0; i < _method.Arguments.size(); i++)
            lines.addAll(_method.Arguments.get(i).getWriteStrings("values[" + i + "]"));

        return lines;
    }

    protected ArrayList<String> getDeserializeArgumentsStrings() {
        ArrayList<String> lines = new ArrayList<String>();

        for (int i = 0; i < _method.Arguments.size(); i++)
            lines.addAll(_method.Arguments.get(i).getReadStrings(_generator, "values[" + i + "]"));

        return lines;
    }

    private void wl(String line) {
        _writer.wl(line);
    }

    private void wl() {
        _writer.wl();
    }
}
