package org.epzilla.dispatcher.dataManager;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 11, 2010
 * Time: 8:28:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterIDGenerator {
    private static String clusterID = "";
    private static int id = 0;
    private static int cID;
    private static HashMap idMap = new HashMap<String, String>();

    public static String getClusterID(String ip) {
        if (idMap.containsKey(ip)) {
            clusterID = (String) idMap.get(ip);
        } else {
            cID = generateID();
            clusterID = "CID" + cID;
            idMap.put(ip, clusterID);
        }
        return clusterID;
    }

    public static void removeClusterID(String ip) {
          idMap.remove(ip);
    }

    private static int generateID() {
        id++;
        return id;
    }
}
