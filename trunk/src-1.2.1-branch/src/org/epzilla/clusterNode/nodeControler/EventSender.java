package org.epzilla.clusterNode.nodeControler;

import org.epzilla.clusterNode.rmi.ClusterInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;

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
    private static Hashtable<String, Object> nodesList = new Hashtable<String, Object>();


    public EventSender() {
    }

    public static void sendEvents(String serverIp, String event) throws RemoteException, MalformedURLException, NotBoundException {
        if (!nodesList.containsKey(serverIp)) {
            initNode(serverIp, "CLUSTER_NODE");
            clusterObj = (ClusterInterface) nodesList.get(serverIp);
            clusterObj.addEventStream(event);
            System.out.println("calling add event stream.");
//            if (response != null) {
//                Logger.log("Events added to the Node " + serverIp);
//            } else {
//                Logger.error("Events adding failure to the Node" + serverIp, null);
//            }
        } else {
            clusterObj = (ClusterInterface) nodesList.get(serverIp);
            clusterObj.addEventStream(event);
            System.out.println("else part working.");

//            if (response != null) {
//                Logger.log("Events added to the Node " + serverIp);
//            } else {
//                Logger.error("Events adding failure to the Node" + serverIp, null);
//            }
        }
    }

    private static void initNode(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);
        nodesList.put(serverIp, obj);

    }

    private static void setClusterObject(Object obj) {
        clusterObj = (ClusterInterface) obj;
    }

}
