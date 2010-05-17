package net.epzilla.stratification.immediate;

import java.util.Hashtable;
import java.util.Vector;


public class SystemVariables {
    private static int numStrata;
    private static Hashtable<Integer, Integer> strataClusters = new Hashtable<Integer, Integer>();
    private static Hashtable<Integer, Hashtable<Integer, Integer>> loadMap = new Hashtable();

    public static int getNumStrata() {
        return numStrata;
    }

    static {

        SystemVariables.setNumStrata(1);
        SystemVariables.setClusters(0, 2);
//        SystemVariables.setClusters(1, 3);
//        SystemVariables.setClusters(2, 1);

        SystemVariables.setClusterLoad(0, 0, 121);
        SystemVariables.setClusterLoad(0, 1, 14);

        Thread t = new Thread() {
            public void run() {
//                SystemVariables
                while (true) {
                    try {
                        SystemVariables.setClusterLoad(0, 0, 121);
                        SystemVariables.setClusterLoad(0, 1, 12);
                        Thread.sleep(40000);
                        SystemVariables.setClusterLoad(0, 0, 12);
                        SystemVariables.setClusterLoad(0, 1, 121);
                        Thread.sleep(40000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }


            }
        };
        t.start();
//        SystemVariables.setClusterLoad(1,0,24);
//        SystemVariables.setClusterLoad(1,1,14);
//        SystemVariables.setClusterLoad(1,2,14);
//        SystemVariables.setClusterLoad(2,0,14);
    }

    public static Hashtable<Integer, Integer> getClusterLoads(int stratum) {
        return loadMap.get(stratum);
    }

    public static void setClusterLoad(int stratum, int cluster, int load) {
        Hashtable<Integer, Integer> map = loadMap.get(stratum);
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
