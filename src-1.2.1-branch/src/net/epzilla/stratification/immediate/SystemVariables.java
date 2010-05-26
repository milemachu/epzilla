package net.epzilla.stratification.dynamic;

import java.util.Hashtable;


public class SystemVariables {
    private static int numStrata;
    private static Hashtable<Integer, Integer> strataClusters = new Hashtable<Integer, Integer>();
    private static Hashtable<Integer, Hashtable<Integer, Integer>> loadMap = new Hashtable();
    public static int count = 0;

    public static volatile int triggerCount = 0;
    public static int roundRobinLimit = 1000;

    //    public static int[] queryLoads = new int[1];
    public static Hashtable<Integer, int[]> triggerLoadMap = new Hashtable();
    public static Hashtable<Integer, Integer> leastLoadClusterMap = new Hashtable();


    public static int getNumStrata() {
        return numStrata;
    }

    static {

        SystemVariables.setNumStrata(1);
        SystemVariables.setClusters(0, 2);

        SystemVariables.setClusterLoad(0, 0, 121);
        SystemVariables.setClusterLoad(0, 1, 14);


//SystemVariables.setNumStrata(2);
//        SystemVariables.setClusters(0, 2);
//        SystemVariables.setClusters(1, 1);
//
//        SystemVariables.setClusterLoad(0, 0, 121);
//        SystemVariables.setClusterLoad(0, 1, 14);
//        SystemVariables.setClusterLoad(1, 0, 14);


        /*
        Thread t = new Thread() {
            public void run() {
//                SystemVariables
                while (true) {
                    try {
                        if (count < 45) {
                            SystemVariables.setClusterLoad(0, 0, 121);
                            SystemVariables.setClusterLoad(0, 1, 12);
                            Thread.sleep(400);
                        } else {
                            SystemVariables.setClusterLoad(0, 0, 12);
                            SystemVariables.setClusterLoad(0, 1, 121);
                            Thread.sleep(400);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }


            }
        };
        t.start();      */
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
        Integer x = leastLoadClusterMap.get(stratum);
        if (x == null) {
            leastLoadClusterMap.put(stratum, cluster);
        } else if (x > load) {
            leastLoadClusterMap.put(stratum, cluster);
        }
    }

    public static void setClusterLoads(int stratum, Hashtable<Integer, Integer> loads) {
        loadMap.put(stratum, loads);
        triggerLoadMap.put(stratum, new int[loads.size()]);
        int min = 0;
        int minVal = Integer.MAX_VALUE;
        for (Integer key : loads.keySet()) {
            int x = loads.get(key);
            if (x < minVal) {
                min = key;
            }
        }

        leastLoadClusterMap.put(stratum, min);
    }

    public static void setNumStrata(int numStrata) {
        SystemVariables.numStrata = numStrata;
    }

    public static int getClusters(int stratum) {
        return strataClusters.get(stratum);
    }

    public static void setClusters(int stratum, int clusters) {
        strataClusters.put(stratum, clusters);

        int[] temp = triggerLoadMap.get(stratum);

        if (temp == null) {
            triggerLoadMap.put(stratum, new int[clusters]);

        } else if ((clusters != temp.length)) {
            int[] temp2 = new int[clusters];
            for (int i = 0; i < temp.length && i < temp2.length; i++) {
                temp2[i] = temp[i];
            }
            triggerLoadMap.put(stratum, temp2);
        }
    }

}
