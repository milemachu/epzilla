/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator.java;

import java.util.ArrayList;

import jstm.generator.Generator;
import jstm.generator.ObjectModelDefinition;
import jstm.generator.Structure;
import jstm.generator.files.FileGenerator;

public class JavaObjectModelWriter extends FileGenerator {

    protected final Generator _generator;

    public JavaObjectModelWriter(Generator generator, ObjectModelDefinition builder, String folder) {
        super(folder, builder.RootPackage.getFullName(), builder.Name);

        _generator = generator;
    }

    @Override
    protected void header() {
        wl("package " + Package + ";");
        wl();
        wl("import jstm.core.*;");
        wl();
        wl("public final class " + Name + " extends jstm.core.ObjectModel {");
        wl();
        wl("    public static final String UID = \"" + _generator.getUID() + "\";");
        wl();
        wl("    public static final String XML = \"" + _generator.getXML() + "\";");
        wl();
        wl("    public " + Name + "() {");
        wl("    }");
        wl();
    }

    @Override
    protected void body() {
        if (_generator.getTarget() == jstm.generator.Generator.Target.Java5)
            wl("    @Override");

        wl("    public String getUID() {");
        wl("        return UID;");
        wl("    }");
        wl("");

        if (_generator.getTarget() == jstm.generator.Generator.Target.Java5)
            wl("    @Override");

        wl("    public String getXML() {");
        wl("        return XML;");
        wl("    }");
        wl("");

        ArrayList<String> names = new ArrayList<String>();

        for (Structure structure : _generator.getObjectModelDefinition().getAllStructures())
            names.add(structure.getFullName());

        if (_generator.getTarget() == jstm.generator.Generator.Target.Java5)
            wl("    @Override");

        wl("    public int getClassCount() {");
        wl("        return " + names.size() + ";");
        wl("    }");
        wl("");

        if (_generator.getTarget() == jstm.generator.Generator.Target.Java5)
            wl("    @Override");

        wl("    public TransactedObject createInstance(int classId, Connection route) {");
        wl("        switch (classId) {");

        for (int i = 0; i < names.size(); i++) {
            wl("            case " + i + ":");
            wl("                return new " + names.get(i) + "();");
        }

        wl("        }");
        wl();
        wl("        throw new IllegalArgumentException(\"Unknown class id: \" + classId);");
        wl("    }");
    }

    @Override
    protected void footer() {
        wl("}");
    }
}
