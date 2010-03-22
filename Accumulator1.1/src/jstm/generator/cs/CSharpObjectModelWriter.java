/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator.cs;

import java.util.ArrayList;

import jstm.generator.Generator;
import jstm.generator.ObjectModelDefinition;
import jstm.generator.Structure;
import jstm.generator.java.JavaObjectModelWriter;

public class CSharpObjectModelWriter extends JavaObjectModelWriter {

    public CSharpObjectModelWriter(Generator generator, ObjectModelDefinition builder, String folder) {
        super(generator, builder, folder);
    }

    @Override
    protected String getExtension() {
        return ".cs";
    }

    @Override
    protected void header() {
        wl("using System;");
        wl("using jstm.core;");
        wl();
        wl("namespace " + Package);
        wl("{");
        wl("	public class " + Name + " : NSTM.Core.ObjectModel {");
        wl();
        wl("        public const string UID_VALUE = \"" + _generator.getUID() + "\";");
        wl();
        wl("        public const string XML_VALUE = \"" + _generator.getXML() + "\";");
        wl();
        wl("		public " + Name + "() {");
        wl("		}");
        wl();

        tab();
    }

    @Override
    protected void body() {
        wl("    public override String getUID() {");
        wl("        return UID_VALUE;");
        wl("    }");
        wl("");
        wl("    public override String getXML() {");
        wl("        return XML_VALUE;");
        wl("    }");
        wl("");

        ArrayList<String> names = new ArrayList<String>();

        for (Structure structure : _generator.getObjectModelDefinition().getAllStructures())
            names.add(structure.getFullName());

        wl("    public override int getClassCount() {");
        wl("        return " + names.size() + ";");
        wl("    }");
        wl("");
        wl("    public override TransactedObject createInstance(int classId, Connection route) {");
        wl("        switch (classId) {");

        for (int i = 0; i < names.size(); i++) {
            wl("            case " + i + ":");
            wl("                return new " + names.get(i) + "();");
        }

        wl("        }");
        wl();
        wl("        throw new ArgumentException(\"Unknown class id: \" + classId);");
        wl("    }");
    }

    @Override
    protected void footer() {
        wl("}");
        untab();
        wl("}");
    }
}
