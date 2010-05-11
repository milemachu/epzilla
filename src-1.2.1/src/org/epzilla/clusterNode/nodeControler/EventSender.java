package org.epzilla.clusterNode.nodeControler;

import org.epzilla.clusterNode.rmi.ClusterInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 7, 2010
 * Time: 9:45:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventSender {
    private static ClusterInterface clusterObj;
    private static String response = null;
    private static String nodeIP, clientID;
    private static ArrayList<String> events;

    public EventSender() {
    }

    public EventSender(String ip, String clientID, ArrayList<String> eventStream) {
        this.nodeIP = ip;
        this.clientID = clientID;
        this.events = eventStream;

    }

    public static void sendEvents() throws RemoteException, MalformedURLException, NotBoundException {
        initNode(nodeIP, "CLUSTER_NODE");

        response = clusterObj.addEventStream(events, clientID);

        if (response != null) {
            System.out.println("Events added to the Node " + nodeIP);
        } else {
            System.err.println("Events adding failure to the Node" + nodeIP);
        }
    }

    private static void initNode(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);

    }

    private static void setClusterObject(Object obj) {
        clusterObj = (ClusterInterface) obj;
    }

}
