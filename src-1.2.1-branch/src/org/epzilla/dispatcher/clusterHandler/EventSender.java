package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.dispatcher.dataManager.EventsCounter;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
//import org.epzilla.util.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 1, 2010
 * Time: 10:21:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventSender {
    private static ClusterInterface clusterObj;
    private static String response = null;

    public EventSender() {
    }

    /*
   event stream need to be add to all the Cluster Nodes
    */
    public static void acceptEvent(ArrayList<String> serverIp, ArrayList<String> clusterID, byte[] event, String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        for (String aServerIp : serverIp) {
            if (!"IP".equalsIgnoreCase(aServerIp)) {
                initCluster(aServerIp, "CLUSTER_NODE");
                String id = "x";
                sendEvent(event, aServerIp, id, clientID);
            }
        }
        Logger.log(Arrays.toString(event));
    }

    private static void initCluster(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);

    }

    private static void sendEvent(byte[] event, String leaderIP, String clusterID, String clientID) throws RemoteException, MalformedURLException, NotBoundException {
        response = clusterObj.acceptEventStream(event, clusterID, clientID);
        
        if (response != null) {
            Logger.log("Event stream send to the Cluster " + clusterID);
        } else {
        }
    }

    private static void setClusterObject(Object obj) {
        clusterObj = (ClusterInterface) obj;
    }

    private static Object getClusterObject() {
        return clusterObj;
    }
}
