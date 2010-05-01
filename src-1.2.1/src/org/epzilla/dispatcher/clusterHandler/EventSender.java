package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.clusterNode.rmi.ClusterInterface;

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
    private static String response = "";

    public EventSender() {
    }
   /*
   event stream need to be add to all the Cluster Nodes
    */
    public static void acceptEventStream(String serverIp, String clusterID, ArrayList<String> eventStream) throws MalformedURLException, NotBoundException, RemoteException {
        initCluster(serverIp, "CLUSTER");
        sendEventStream(eventStream, clusterID);
    }

    private static void initCluster(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);

    }

    private static void sendEventStream(ArrayList<String> events, String cID) throws RemoteException, MalformedURLException, NotBoundException {
        response = null;
        response = clusterObj.acceptEventStream(events, cID);
        if (response != null)
            System.out.println("Event stream send to the cluster");
        else
            System.out.println("Event stream not accepted");
    }

    private static void setClusterObject(Object obj) {
        clusterObj = (ClusterInterface) obj;
    }

    private static Object getClusterObject() {
        return clusterObj;
    }
}
