package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.clusterNode.rmi.ClusterInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: Mar 11, 2010
 * Time: 1:17:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerSender {
    private static ClusterInterface clusterObj;

    public TriggerSender() {
    }

    public static void acceptTrigger(String serverIp, String clusterID, ArrayList<String> triggers) throws MalformedURLException, NotBoundException, RemoteException {
        initCluster(serverIp, "Cluster");
        System.out.println(serverIp);
        sendTriggers(triggers, clusterID);
    }

    private static void initCluster(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + "Cluster";
        ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);

    }

    private static void sendTriggers(ArrayList<String> triggers, String cID) throws RemoteException, MalformedURLException, NotBoundException {
        String response = null;
        response = clusterObj.acceptTiggerStream(triggers, cID);
        if (response != null)
            System.out.println("Triggers send to the cluster");
        else
            System.out.println("Triggers not accepted");
    }

    private static void setClusterObject(Object obj) {
        clusterObj = (ClusterInterface) obj;
    }

    private static Object getClusterObject() {
        return clusterObj;
    }

}
