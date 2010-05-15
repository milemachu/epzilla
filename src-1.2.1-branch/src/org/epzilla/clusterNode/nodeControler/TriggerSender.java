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
 * Time: 9:45:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerSender {
    private static ClusterInterface clusterObj;
    private static String response = null;
    private static String nodeIP, clientID;
    private static ArrayList<String> triggers;

    public TriggerSender() {
    }

    public TriggerSender(String ip, String clientID, ArrayList<String> triggerList) {
        this.nodeIP = ip;
        this.clientID = clientID;
        this.triggers = triggerList;
    }


    private static void initNode(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);

    }

    private static void sendTriggers(ArrayList<String> triggers, String clientID) throws RemoteException, MalformedURLException, NotBoundException {
        initNode(nodeIP, "CLUSTER_NODE");

        response = clusterObj.addTriggerStream(triggers, clientID);
        if (response != null) {
            System.out.println("Triggers send to the Node:" + nodeIP);
        } else {
            System.out.println("Triggers not accepted");
        }
    }

    private static void setClusterObject(Object obj) {
        clusterObj = (ClusterInterface) obj;
    }
}
