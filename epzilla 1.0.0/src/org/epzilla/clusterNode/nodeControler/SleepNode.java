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

import org.epzilla.clusterNode.NodeController;
import org.epzilla.clusterNode.dataManager.NodeManager;
import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.daemon.services.DaemonSleepCaller;
import org.epzilla.leader.LeaderElectionInitiator;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * This class is to sleep a Node according to the performance information
 * received from the Node Leader.
 * Author: Chathura
 * Date: Jun 11, 2010
 * Time: 12:05:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SleepNode {
    private static boolean success = false;
   /*
   sleep Node
   check whether there is more than 2 nodes in a particular Node Cluster and if it is greater than 2 then sleep a Node 
    */
    public static void sleep() {
        try {
            HashSet<String> nodeList = new HashSet<String>();
            nodeList = LeaderElectionInitiator.getNodes();
            if (nodeList.size() > 2) {
                String leaderIP = NodeController.getLeaderIP();
                for (Iterator i = nodeList.iterator(); i.hasNext();) {
                    String ip = (String) i.next();
                    if (!ip.equalsIgnoreCase(leaderIP)) {
                        DaemonSleepCaller sleepingAgent = new DaemonSleepCaller();
                        success = sleepingAgent.callSleep(ip);
                        NodeManager.addIP("", ip);
                        if (success)
                            NodeUIController.appendTextToStatus("Sleep the Node: " + ip + " successfully");
                    }
                    break;
                }
            }
            if (!success) {
                NodeUIController.appendTextToStatus("There aren't any  Nodes to Sleep...");
            }
        } catch (Exception e) {
            NodeUIController.appendTextToStatus("There aren't any  Nodes to Sleep...");
        }
    }
}
