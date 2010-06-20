package org.epzilla.clusterNode.clusterObjectModelEngine;

import jstm.generator.*;
import jstm.generator.Package;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Mar 3, 2010
 * Time: 9:13:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterObjectModelGenerator {
    private static ObjectModelDefinition create() {
        ObjectModelDefinition model = new ObjectModelDefinition("ClusterObjectModel");
        jstm.generator.Package pack = new Package("clusterInfoObjectModel");
        model.RootPackage = pack;

        Structure simple = new Structure("TriggerObject");
        simple.Fields.add(new Field(String.class, "clientID"));
        simple.Fields.add(new Field(String.class, "triggerID"));
        simple.Fields.add(new Field(String.class, "trigger"));

        Structure ip = new Structure("NodeIPObject");
        ip.Fields.add(new Field(String.class, "nodeID"));
        ip.Fields.add(new Field(String.class, "clusterID"));
        ip.Fields.add(new Field(String.class, "IP"));
         ip.Fields.add(new Field(Boolean.class, "IsActive"));

        Structure performanceInfo = new Structure("PerformanceInfoObject");
        performanceInfo.Fields.add(new Field(String.class, "nodeIP"));
        performanceInfo.Fields.add(new Field(String.class, "CPUusageAverage"));
        performanceInfo.Fields.add(new Field(String.class, "MemUsageAverage"));
        performanceInfo.Fields.add(new Field(String.class, "NetworkUsageAverage"));

        pack.Structures.add(simple);
        pack.Structures.add(ip);
        pack.Structures.add(performanceInfo);
        return model;
    }


    public static void main(String[] args) {
        ObjectModelDefinition model = create();
        Generator generator = new Generator(model);
        generator.writeFiles("./src/org/epzilla/clusterNode/", Generator.Target.Java5, false);
    }
}
