package org.epzilla.clusterNode.userInterface;

import org.epzilla.clusterNode.dataManager.ClusterIPManager;
import org.epzilla.leader.LeaderElectionInitiator;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * This class is to manage the IP lists of the Cluster Leader
 * Author: Chathura
 * Date: May 31, 2010
 * Time: 2:14:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class IpListManager {
    private static int INIT_TIME_INTERVAL = 10000;
    private static int UPDATE_TIME_INTERVAL = 60000;

    public static void Initialize() {
        initNodeIpList();
    }

    private static void initClientIpList() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                HashSet<String> ipList = LeaderElectionInitiator.getSubscribedNodeList();
                HashSet<String> nodeList = LeaderElectionInitiator.getNodes();

                String currentList = NodeUIController.getIpList();
                String cList = NodeUIController.getNodeList();

                if (ipList != null) {
                    for (Iterator i = ipList.iterator(); i.hasNext();) {
                        String ip = (String) i.next();
                        if (!currentList.contains(ip))
                            NodeUIController.appendTextToIPList(ip);
                    }
                }
                System.gc();
            }
        }, INIT_TIME_INTERVAL, UPDATE_TIME_INTERVAL);
    }
    /*
   take subscribed node list periodically from the Leader election initiator
    */

    private static void initNodeIpList() {
        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                HashSet<String> nodeList = LeaderElectionInitiator.getSubscribedNodeList();
                int clusterID = LeaderElectionInitiator.getClusterId();
                if (nodeList != null) {
                    NodeUIController.clearNodeList();
                    ClusterIPManager.clearIPList();
                    for (Iterator i = nodeList.iterator(); i.hasNext();) {
                        String ip = (String) i.next();
                        NodeUIController.appendTextToNodeList(ip);
                        ClusterIPManager.addIP("" + clusterID, ip);
                    }
                    System.gc();
                }
            }
        }, INIT_TIME_INTERVAL, UPDATE_TIME_INTERVAL);
    }
}
