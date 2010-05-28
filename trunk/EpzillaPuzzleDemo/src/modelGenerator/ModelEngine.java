package modelGenerator;

import jstm.generator.*;
import jstm.generator.Package;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 28, 2010
 * Time: 11:20:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class ModelEngine {
    public static ObjectModelDefinition create() {
        ObjectModelDefinition model = new ObjectModelDefinition("ImagesObjectModel");

        Package pack = new Package("generatedModel");
        model.RootPackage = pack;

        Structure simple = new Structure("ImageInfo");
        simple.Fields.add(new Field(String.class, "Url"));
        simple.Fields.add(new Field(int.class, "Left"));
        simple.Fields.add(new Field(int.class, "Top"));
        simple.Fields.add(new Field(int.class, "zOrder"));
        pack.Structures.add(simple);

        return model;
    }

    public static void main(String[] args) throws Exception {
        ObjectModelDefinition model = create();
        Generator generator = new Generator(model);
        generator.writeFiles("./src", Generator.Target.Java5, false);
    }
}
