package org.epzilla.dispatcher.objectModelGenerator;

import jstm.generator.*;
import jstm.generator.Package;


/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Feb 18, 2010
 * Time: 4:01:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModelGenerationEngine {


    	private static ObjectModelDefinition create() {
		ObjectModelDefinition model = new ObjectModelDefinition("DispatcherObjectModel");
		Package pack = new Package("dispatcherObjectModel");
		model.RootPackage = pack;

		Structure simple = new Structure("TriggerInfoObject");
		simple.Fields.add(new Field(String.class, "triggerID"));
		simple.Fields.add(new Field(String.class,"trigger"));

		pack.Structures.add(simple);
		return model;
	}


    	public static void main(String[] args) {
		ObjectModelDefinition model = create();
		Generator generator = new Generator(model);
		generator.writeFiles("./src/org/epzilla/dispatcher/", Generator.Target.Java5, false);
	}

}
