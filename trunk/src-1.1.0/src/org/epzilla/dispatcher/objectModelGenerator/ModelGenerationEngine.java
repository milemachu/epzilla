package org.epzilla.dispatcher.objectModelGenerator;

import jstm.generator.*;
import jstm.generator.Package;


public class ModelGenerationEngine {


    private static ObjectModelDefinition create() {
        ObjectModelDefinition model = new ObjectModelDefinition("DispatcherObjectModel");
        Package pack = new Package("dispatcherObjectModel");
        model.RootPackage = pack;

        Structure simple = new Structure("TriggerInfoObject");
        simple.Fields.add(new Field(String.class, "triggerID"));
        simple.Fields.add(new Field(String.class, "trigger"));

        Structure client = new Structure("ClientInfoObject");
        client.Fields.add(new Field(String.class, "clientID"));
        client.Fields.add(new Field(String.class, "clientIP"));

        pack.Structures.add(simple);
        pack.Structures.add(client);
        return model;
    }


    public static void main(String[] args) {
        ObjectModelDefinition model = create();
        Generator generator = new Generator(model);
        generator.writeFiles("./src/org/epzilla/dispatcher/", Generator.Target.Java5, false);
    }

}
