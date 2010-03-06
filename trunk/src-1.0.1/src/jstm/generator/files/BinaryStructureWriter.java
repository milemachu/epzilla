/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator.files;

import jstm.generator.Generator;
import jstm.generator.ReturnValue;
import jstm.generator.Structure;

public abstract class BinaryStructureWriter extends StructureWriter {

    protected BinaryStructureWriter(Generator generator, Structure structure, String folder) {
        super(generator, structure, folder);
    }

    protected abstract String getMaxShort();

    @Override
    protected void writeSerialization() {
        if (getJava5())
            wl("    @Override");

        wl("    protected" + overrideString() + "void serialize(TransactedObject.Version version, Writer writer) " + throwsString() + "{");
        wl("        " + booleanString() + "[] reads = ((TransactedStructure.Version) version).getReads();");
        wl("        Object[] values = ((TransactedStructure.Version) version).getWrites();");
        wl();
        wl("        if (reads != null) {");
        wl("            writer.writeShort(" + getMaxShort() + ");");
        wl();
        wl("            for (int i = 0; i < " + _structure.Fields.size() + "; i++)");
        wl("                writer.writeBoolean(reads[i]);");
        wl("        }");
        wl();
        wl("        if (values != null) {");

        for (int i = 0; i < _structure.Fields.size(); i++) {
            wl("            if (values[" + i + "] != null) {");
            wl("                if (values[" + i + "] == Removal.Instance)");
            wl("                    writer.writeShort((short) " + -(i + 1) + ");");
            wl("                else {");
            wl("                    writer.writeShort((short) " + (i + 1) + ");");
            wl("                    " + _structure.Fields.get(i).Type.getWriteString("values[" + i + "]"));
            wl("                }");
            wl("            }");

            if (i < _structure.Fields.size() - 1)
                wl();
        }

        wl("        }");
        wl();
        wl("        writer.writeShort((short) 0);");
        wl("    }");
        wl();

        if (getJava5()) {
            wl("    @SuppressWarnings(\"null\")");
            wl("    @Override");
        }

        wl("    protected" + overrideString() + "void deserialize(TransactedObject.Version version, Reader reader) " + throwsString() + "{");
        wl("        " + booleanString() + "[] reads = null;");
        wl("        Object[] values = null;");
        wl();
        wl("        short index = reader.readShort();");
        wl();
        wl("        if (index == " + getMaxShort() + ") {");
        wl("            reads = new " + booleanString() + "[" + _structure.Fields.size() + "];");
        wl();
        wl("            for (int i = 0; i < " + _structure.Fields.size() + "; i++)");
        wl("                reads[i] = reader.readBoolean();");
        wl();
        wl("            index = reader.readShort();");
        wl("        }");
        wl();

        for (int i = 0; i < _structure.Fields.size(); i++) {
            wl("        if (index == " + (i + 1) + ") {");
            wl("            if (values == null)");
            wl("                values = new Object[" + _structure.Fields.size() + "];");
            wl();
            wl("            values[" + i + "] = " + _structure.Fields.get(i).Type.getReadString());
            wl("            index = reader.readShort();");
            wl("        } else if (index == " + -(i + 1) + ") {");
            wl("            if (values == null)");
            wl("                values = new Object[" + _structure.Fields.size() + "];");
            wl();
            wl("            values[" + i + "] = Removal.Instance;");
            wl("            index = reader.readShort();");
            wl("        }");
            wl();
        }

        wl("        ((TransactedStructure.Version) version).setReads(reads);");
        wl("        ((TransactedStructure.Version) version).setWrites(values);");
        wl("    }");
    }

    @Override
    protected void writeArgumentsSerialization() {
        if (getJava5())
            wl("    @Override");

        wl("    protected" + overrideString() + "void serializeArguments(int index, Object[] values, Writer writer) " + throwsString() + "{");
        wl("        switch (index) {");

        for (int i = 0; i < _structure.Methods.size(); i++) {
            MethodWriter writer = new MethodWriter(this, _structure.Methods.get(i));

            wl("            case " + i + ": {");

            for (String line : writer.getSerializeArgumentsStrings())
                wl("                " + line);

            wl("                break;");
            wl("            }");
        }

        wl("            default:");
        wl("                throw new java.lang.IllegalStateException();");
        wl("        }");
        wl("    }");
        wl();

        if (getJava5())
            wl("    @Override");

        wl("    protected" + overrideString() + "Object[] deserializeArguments(int index, Reader reader) " + throwsString() + "{");
        wl("        switch (index) {");

        for (int i = 0; i < _structure.Methods.size(); i++) {
            MethodWriter writer = new MethodWriter(this, _structure.Methods.get(i));

            wl("            case " + i + ": {");
            wl("                Object[] values = new Object[" + _structure.Methods.get(i).Arguments.size() + "];");

            for (String line : writer.getDeserializeArgumentsStrings())
                wl("                " + line);

            wl("                return values;");
            wl("            }");
        }

        wl("            default:");
        wl("                throw new java.lang.IllegalStateException();");
        wl("        }");
        wl("    }");
    }

    @Override
    protected void writeResultsSerialization() {
        if (getJava5())
            wl("    @Override");

        wl("    protected" + overrideString() + "void serializeResult(int index, Object result, Writer writer) " + throwsString() + "{");
        wl("        switch (index) {");

        for (int i = 0; i < _structure.Methods.size(); i++) {
            wl("            case " + i + ": {");

            ReturnValue value = _structure.Methods.get(i).ReturnValue;

            if (value.Type.getNonGeneratedClass() != void.class)
                for (String line : value.getWriteStrings("result"))
                    wl("                " + line);

            wl("                break;");
            wl("            }");
        }

        wl("            default:");
        wl("                throw new java.lang.IllegalStateException();");
        wl("        }");
        wl("    }");
        wl();

        if (getJava5()) {
            wl("    @SuppressWarnings(\"unchecked\")");
            wl("    @Override");
        }

        wl("    protected" + overrideString() + "Object deserializeResult(int index, Reader reader) " + throwsString() + "{");
        wl("        switch (index) {");

        for (int i = 0; i < _structure.Methods.size(); i++) {
            wl("            case " + i + ": {");

            ReturnValue value = _structure.Methods.get(i).ReturnValue;

            if (value.Type.getNonGeneratedClass() != void.class) {
                wl("                Object value;");

                for (String line : value.getReadStrings(_generator, "value"))
                    wl("                " + line);

                wl("                return value;");
            } else
                wl("                return null;");

            wl("            }");
        }

        wl("            default:");
        wl("                throw new java.lang.IllegalStateException();");
        wl("        }");
        wl("    }");
    }
}
