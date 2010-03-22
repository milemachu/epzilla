/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator.cs;

import jstm.generator.Field;
import jstm.generator.Generator;
import jstm.generator.Structure;
import jstm.generator.files.BinaryStructureWriter;

public class CSharpStructureWriter extends BinaryStructureWriter {

    public CSharpStructureWriter(Generator generator, Structure structure, String folder) {
        super(generator, structure, folder);
    }

    @Override
    protected String getExtension() {
        return ".cs";
    }

    @Override
    public String finalString() {
        return "readonly";
    }

    @Override
    public String overrideString() {
        return " override ";
    }

    @Override
    protected String throwsString() {
        return "";
    }

    @Override
    public String booleanString() {
        return "bool";
    }

    @Override
    protected String getMaxShort() {
        return "short.MaxValue";
    }

    @Override
    protected void header() {
        wl("using System;");
        wl("using jstm.misc;");
        wl("using jstm.core;");
        wl("using java.util;");

        wl();
        wl("namespace " + Package);
        wl("{");

        tab();

        String base = _structure.Parent != null ? _structure.Parent.getFullName() : "NSTM.Core.TransactedStructure";

        wl("public class " + Name + " : " + base);
        wl("{");

        wl("	public " + Name + "()");
        wl("		: base( " + _structure.Fields.size() + " )");
        wl("	{");
        wl("	}");
    }

    @Override
    protected void writeField(int index) {
        Field field = _structure.Fields.get(index);

        if (field.Description != null && field.Description.length() > 0)
            wl("		/** " + field.Description + " */");

        wl("	public " + field.Type.getFullName(_generator) + " " + field.Name);
        wl("	{");
        wl("		get");
        wl("		{");

        tab();
        writeFieldGet(index);
        untab();

        wl("		}");
        wl("		set");
        wl("		{");

        String big = getBigType(field);

        if (big != null)
            wl("			set(" + index + ", new " + big + "(value));");
        else
            wl("			set(" + index + ", value);");

        wl("		}");
        wl("	}");
        wl();
    }

    @Override
    protected void footer() {
        untab();

        wl("}");
    }
}
