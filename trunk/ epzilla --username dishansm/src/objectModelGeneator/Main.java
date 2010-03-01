package objectModelGeneator;

import jstm.generator.*;
import jstm.generator.Package;

public class Main {

	public static ObjectModelDefinition create() {
		ObjectModelDefinition model = new ObjectModelDefinition("PerformanceTesterObjectModel");
		Package pack = new Package("generated");
		model.RootPackage = pack;
		
		Structure simple = new Structure("TestObject");
		simple.Fields.add(new Field(String.class, "Name")); 
		simple.Fields.add(new Field(String.class,"Text"));

		pack.Structures.add(simple);
		return model;
	}

	public static void main(String[] args) {
		ObjectModelDefinition model = create();
		Generator generator = new Generator(model);
		generator.writeFiles("./src", Generator.Target.Java5, false);
		
//        File xml = new File("src/objectModelGeneator/ObjectModel.xml");
//        ObjectModelDefinition model = ObjectModelDefinition.fromXML(xml);
//        Package pack = new Package("generated");
//    	model.RootPackage = pack;
//        Generator generator = new Generator(model);
//        generator.writeFiles("./src", Generator.Target.Java5, false);
	}

}
