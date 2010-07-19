package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;
//import org.epzilla.util.Logger;

/**
 * Created by IntelliJ IDEA.
 * This class is to send the Events to the Cluster leader
 * Author: Chathura
 * Date: May 1, 2010
 * Time: 10:21:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventSender {
    private static ClusterInterface clusterObj;
    private static String response = null;
    private static String serviceName = "CLUSTER_NODE";
    private static Hashtable<String, Object> leaderList = new Hashtable<String, Object>();

    public EventSender() {
    }

    /**
     * Method create remote reference to the Cluster Leaders
     * @param serverIp
     * @param serviceName
     * @return
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    private static ClusterInterface initCluster(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);
        leaderList.put(serverIp, obj);
        return obj;

    }

    /**
     * Method for sending Events to the Cluster Node Leaders
     * @param event
     * @param leaderIP
     * @param clusterID
     * @param clientID
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    public static void sendEvent(byte[] event, String leaderIP, String clusterID, String clientID) throws RemoteException, MalformedURLException, NotBoundException {
//        String cid = "x";
        if (!"IP".equalsIgnoreCase(leaderIP)) {
            if (!leaderList.containsKey(leaderIP)) {
                ClusterInterface clusterObj = initCluster(leaderIP, serviceName);
                response = clusterObj.acceptEventStream(event, clusterID);

                if (response != null) {
                    Logger.log("Event stream send to the Cluster " + clusterID);
                } else {
                    Logger.error("Events adding failure to theCluster" + leaderIP, null);
                }
            } else {
                clusterObj = (ClusterInterface) leaderList.get(leaderIP);
                response = clusterObj.acceptEventStream(event, clusterID);

                if (response != null) {
                    Logger.log("Event stream send to the Cluster " + clusterID);
                }
            }
        }
    }

    private static void setClusterObject(Object obj) {
        clusterObj = (ClusterInterface) obj;
    }

    private static Object getClusterObject() {
        return clusterObj;
    }
}
