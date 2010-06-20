package org.epzilla.clusterNode.nodeControler;

import org.epzilla.clusterNode.dataManager.ClusterIPManager;
import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.clusterNode.userInterface.NodeUIController;
import org.epzilla.daemon.services.DaemonWakeCaller;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: May 25, 2010
 * Time: 1:45:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class WakeNode {
    private static String serviceName = "CLUSTER_NODE";
    private static boolean success = false;
    private static ArrayList<String> nodeIPList = new ArrayList<String>();

    public static void wake() {
        try {
            nodeIPList.clear();
            nodeIPList = ClusterIPManager.getNodeIpList();

            for (Iterator i = nodeIPList.iterator(); i.hasNext();) {
                String ip = (String) i.next();
                boolean status = ClusterIPManager.getNodeStatus(ip);

                if (!status) {
                    DaemonWakeCaller wakingAgent = new DaemonWakeCaller();
                    success = wakingAgent.callWake(ip);
                    if (success)
                        NodeUIController.appendTextToStatus("Wake the Node: " + ip + " successfully");
                    break;
                }
            }
            if (!success) {
                NodeUIController.appendTextToStatus("There are no idle Nodes to wake up...");
            }
        } catch (Exception e) {
            NodeUIController.appendTextToStatus("There are no idle Nodes to wake up...");
        }
        success = false;
    }

    public static void nodeInit(String nodeIP) {
        try {
            ClusterInterface nodeObj = initService(nodeIP, serviceName);
            nodeObj.initNodeProcess();
        } catch (MalformedURLException e) {
            Logger.error("", e);
        } catch (NotBoundException e) {
            Logger.error("", e);
        } catch (RemoteException e) {
            Logger.error("", e);
        }
    }

    private static ClusterInterface initService(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        return (ClusterInterface) Naming.lookup(url);
    }
}
