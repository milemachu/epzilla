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
package org.epzilla.clusterNode.nodeControler;

import org.epzilla.clusterNode.dataManager.ClusterIPManager;
import org.epzilla.clusterNode.dataManager.NodeManager;
import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.daemon.services.DaemonWakeCaller;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * This class is to wake a Node which went to idle state. 
 * Author: Chathura
 * Date: May 25, 2010
 * Time: 1:45:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class WakeNode {
    private static String serviceName = "CLUSTER_NODE";
    private static boolean success = false;
    private static ArrayList<String> nodeIPList = new ArrayList<String>();
    /*
    wake a Node
    get the details of the sleep nodes and wake a Node from that list
     */

    public static void wake() {
        try {
            nodeIPList.clear();
            nodeIPList = ClusterIPManager.getNodeIpList();

            String ipToWake = NodeManager.removeIP();
            if (ipToWake != null) {
//            for (Iterator i = nodeIPList.iterator(); i.hasNext();) {
//                String ip = (String) i.next();
//                boolean status = ClusterIPManager.getNodeStatus(ip);
//                if (!status) {
                DaemonWakeCaller wakingAgent = new DaemonWakeCaller();
                success = wakingAgent.callWake(ipToWake);
//                    ClusterIPManager.setNodeStatus(ip,true);
                if (success)
                    NodeUIController.appendTextToStatus("Wake the Node: " + ipToWake + " successfully");
//                    break;
//                }
            }
            if (!success) {
                NodeUIController.appendTextToStatus("There are no idle Nodes to wake up...");
            }
        } catch (Exception e) {
            NodeUIController.appendTextToStatus("There are no idle Nodes to wake up...");
        }
        success = false;
    }
}
