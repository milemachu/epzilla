/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.generator.java;

import jstm.generator.Generator;
import jstm.generator.Method;
import jstm.generator.Structure;
import jstm.generator.files.BinaryStructureWriter;

public class JavaStructureWriter extends BinaryStructureWriter {

    public JavaStructureWriter(Generator generator, Structure structure, String folder) {
        super(generator, structure, folder);
    }

    @Override
    protected void header() {
        wl("package " + Package + ";");
        wl();
        wl("import jstm.core.*;");

        if (_structure.Methods.size() > 0) {
            for (Method method : _structure.Methods) {
                if (method.hasList()) {
                    wl("import java.util.List;");
                    break;
                }
            }

            wl("import jstm.core.MethodCallback;");
        }

        String ext = _structure.Parent != null ? _structure.Parent.getFullName() : "jstm.core.TransactedStructure";

        wl();
        wl("public class " + Name + " extends " + ext + " {");
        wl();
        wl("    public " + Name + "() {");
        wl("        super(FIELD_COUNT);");
        wl("    }");
    }

    @Override
    protected String getMaxShort() {
        return "Short.MAX_VALUE";
    }
}
