package org.epzilla.accumulator.stmgen;

import org.epzilla.accumulator.global.Event;

import jstm.generator.*;
import jstm.generator.Package;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Mar 4, 2010
 * Time: 6:43:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class StmGenerator {


    public static void main(String[] args) {
        ObjectModelDefinition model = new ObjectModelDefinition("AccumulatorObjectModel");
        jstm.generator.Package pack = new Package("net.epzilla.accumulator.generated");
        model.RootPackage = pack;

        Structure simple = new Structure("SharedDerivedEvent");

        simple.Fields.add(new Field(Long.class, "srcId"));
        simple.Fields.add(new Field(Long.class, "id"));
        simple.Fields.add(new Field(Integer.class, "clientId"));
        simple.Fields.add(new Field(String.class, "content"));
           
        pack.Structures.add(simple);

        Structure marker = new Structure("StructureMarker");
        marker.Parent = simple;
        marker.Fields.add(new Field(String.class, "structureType"));
        marker.Fields.add(new Field(String.class, "structureId"));

        pack.Structures.add(marker);

        Generator generator = new Generator(model);
        generator.writeFiles("./src/", Generator.Target.Java5, false);


    }
}
