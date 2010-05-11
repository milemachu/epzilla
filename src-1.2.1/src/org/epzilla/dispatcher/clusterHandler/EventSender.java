package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.dispatcher.dataManager.EventsCounter;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

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
    public static void acceptEventStream(ArrayList<String> serverIp, ArrayList<String> clusterID, ArrayList<String> eventStream, String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        for (int i = 0; i < clusterID.size(); i++) {
            initCluster(serverIp.get(i), "CLUSTER_LEADER");
            sendEventStream(eventStream, serverIp.get(i), clusterID.get(i), clientID);
        }

    }

    private static void initCluster(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);

    }

    private static void sendEventStream(ArrayList<String> eList, String leaderIP, String clusterID, String clientID) throws RemoteException, MalformedURLException, NotBoundException {
        response = clusterObj.acceptEventStream(eList, clusterID, clientID);
        EventsCounter.setOutEventCount(eList.size());
        if (response != null) {
            System.out.println("Event stream send to the Cluster " + clusterID);
        } else {
            LeaderDisconnector led = new LeaderDisconnector();
            led.leaderRemover(leaderIP);
        }
    }

    private static void setClusterObject(Object obj) {
        clusterObj = (ClusterInterface) obj;
    }

    private static Object getClusterObject() {
        return clusterObj;
    }
}
