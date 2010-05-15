package net.epzilla.stratification.immediate;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 12, 2010
 * Time: 11:08:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class SystemVariables {
    private static int numStrata;
    private static Hashtable<Integer, Integer> strataClusters = new Hashtable<Integer, Integer>();
    private static Hashtable<Integer, Hashtable<Integer, Integer>> loadMap = new Hashtable();

    public static int getNumStrata() {
        return numStrata;
    }

    static {

        SystemVariables.setNumStrata(3);
        SystemVariables.setClusters(0, 2);
        SystemVariables.setClusters(1, 3);
        SystemVariables.setClusters(2, 1);

        SystemVariables.setClusterLoad(0,0,121);
        SystemVariables.setClusterLoad(0,1,14);
        SystemVariables.setClusterLoad(1,0,24);
        SystemVariables.setClusterLoad(1,1,14);
        SystemVariables.setClusterLoad(1,2,14);
        SystemVariables.setClusterLoad(2,0,14);
    }

    public static Hashtable<Integer, Integer> getClusterLoads(int stratum) {
        return loadMap.get(stratum);
    }

    public static void setClusterLoad(int stratum, int cluster, int load) {
        Hashtable<Integer,Integer> map = loadMap.get(stratum);
        if (map == null) {
            map = new Hashtable();
            loadMap.put(stratum, map);
        }
        map.put(cluster, load);
    }

    public static void setClusterLoads(int stratum, Hashtable<Integer, Integer> loads) {
        loadMap.put(stratum, loads);
    }

    public static void setNumStrata(int numStrata) {
        SystemVariables.numStrata = numStrata;
    }

    public static int getClusters(int stratum) {
        return strataClusters.get(stratum);
    }

    public static void setClusters(int stratum, int clusters) {
        strataClusters.put(stratum, clusters);

    }

}
