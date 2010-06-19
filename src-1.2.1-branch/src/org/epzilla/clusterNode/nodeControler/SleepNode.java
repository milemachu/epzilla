package org.epzilla.clusterNode.nodeControler;

import org.epzilla.clusterNode.NodeController;
import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.leader.LeaderElectionInitiator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: Jun 11, 2010
 * Time: 12:05:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class SleepNode {
    private static String serviceName = "CLUSTER_NODE";
    private static boolean success = false;

    public static void sleep() {
        try {
            HashSet<String> nodeList = new HashSet<String>();
            nodeList = LeaderElectionInitiator.getNodes();
            if (nodeList.size() > 2) {
                String leaderIP = NodeController.getLeaderIP();
                for (Iterator i = nodeList.iterator(); i.hasNext();) {
                    String ip = (String) i.next();
                    if (!ip.equalsIgnoreCase(leaderIP)) {
                        sleepNode(ip);
                        NodeUIController.appendTextToStatus("Sleep the Node: " + ip + " successfully");
                        success = true;
                    }
                    break;
                }
            }
            if (!success) {
                NodeUIController.appendTextToStatus("There aren't any  Nodes to Sleep...");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            NodeUIController.appendTextToStatus("There aren't any  Nodes to Sleep...");
        }
    }

    public static void sleepNode(String nodeIP) {
        try {
            ClusterInterface clusterObj = initService(nodeIP, serviceName);
            clusterObj.sleepNodeProcess();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static ClusterInterface initService(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        return (ClusterInterface) Naming.lookup(url);
    }
}
