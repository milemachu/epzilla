package org.epzilla.clusterNode.nodeControler;

import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.util.Logger;

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
    private static String clientID;
    private static ArrayList<String> events;
    static ArrayList<String> serverIp;

    public EventSender() {
    }

    public EventSender(ArrayList<String> serverIp, String clientID, ArrayList<String> eventStream) {
        this.serverIp = serverIp;
        this.clientID = clientID;
        this.events = eventStream;

    }

    public static void sendEvents() throws RemoteException, MalformedURLException, NotBoundException {
        for (int i = 0; i < serverIp.size(); i++) {
            initNode(serverIp.get(i), "CLUSTER_NODE");
            response = clusterObj.addEventStream(events, clientID);

            if (response != null) {
                Logger.log("Events added to the Node " + serverIp.get(i));
            } else {
                Logger.error("Events adding failure to the Node" + serverIp.get(i), null);
            }
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
