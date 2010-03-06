/**
 * JSTM (http://jstm.sourceforge.net)
 * Distributed under the Apache License Version 2.0
 * Copyright © 2006-2007 Cyprien Noel
 */

package jstm.tests.generated;

import jstm.generator.*;
import jstm.generator.Package;

public class Main {

    public static void main(String[] args) throws Exception {
        ObjectModelDefinition model = getObjectModelDefinition();
        Generator generator = new Generator(model);
        generator.writeFiles("./src", Generator.Target.Java5, false);
    }

    public static ObjectModelDefinition getObjectModelDefinition() {
        ObjectModelDefinition model = new ObjectModelDefinition("SimpleObjectModel");

        Package root = new Package("jstm.tests.generated");
        model.RootPackage = root;

        Structure simple = new Structure("SimpleStructure");
        simple.Fields.add(new Field(String.class, "Text"));
        simple.Fields.add(new Field(Integer.class, "Int"));
        simple.Fields.add(new Field(Double.class, "Double"));
        simple.Fields.add(new Field(simple, "Entity"));

        Field cyclic = new Field();
        cyclic.Name = "Entity2";
        cyclic.Type = new Type();
        cyclic.Type.GeneratedClass = "jstm.tests.generated.test.SimpleStructure2";
        simple.Fields.add(cyclic);

        root.Structures.add(simple);

        Method method = new Method("method");
        method.ReturnValue = new ReturnValue(String.class);
        method.Arguments.add(new Argument(String.class, "sql"));
        method.Arguments.add(new Argument(simple, "eg"));
        simple.Methods.add(method);

        Package pack2 = new Package("test");
        root.Packages.add(pack2);

        Structure simple2 = new Structure("SimpleStructure2");
        simple2.Fields.add(new Field(String.class, "Text"));
        simple2.Fields.add(new Field(Integer.class, "Int"));
        simple2.Fields.add(new Field(Double.class, "Double"));
        simple2.Fields.add(new Field(simple, "Entity"));
        pack2.Structures.add(simple2);

        Method method2 = new Method("method2");
        method2.ReturnValue = new ReturnValue(String.class);
        method2.Arguments.add(new Argument(String.class, "sql"));
        simple2.Methods.add(method2);

        return model;
    }
}
