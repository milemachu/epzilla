/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.dispatcher.dataManager;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * This class is to generate the Cluster ID
 * Author: Chathura
 * Date: May 11, 2010
 * Time: 8:28:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterIDGenerator {
    private static String clusterID = "";
    private static int id = 0;
    private static int cID;
    private static HashMap idMap = new HashMap<String, String>();
    /*
   method generates cluster ID
    */

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
