package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: Mar 11, 2010
 * Time: 1:17:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerSender {
    private static HashMap idMap = new HashMap<String, String>();
    private static ClusterInterface clusterObj;
    private static String response = null;
    private static TriggerSender instance = new TriggerSender();

    public TriggerSender() {
    }

    public static TriggerSender getInstance() {
        return instance;
    }

    public static void acceptTrigger(String serverIp, String clusterID, ArrayList<String> triggers, String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        if (idMap.containsKey(serverIp)) {
            clusterObj = (ClusterInterface) idMap.get(serverIp);
            sendTriggers(triggers, clusterID, clientID);
        } else {
            initCluster(serverIp, "CLUSTER_NODE");
            sendTriggers(triggers, clusterID, clientID);
        }

    }

    private static void initCluster(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClusterInterface obj = (ClusterInterface) Naming.lookup(url);
        setClusterObject(obj);
        idMap.put(serverIp,clusterObj);

    }

    private static void sendTriggers(ArrayList<String> triggers, String clusterID, String clientID) throws RemoteException, MalformedURLException, NotBoundException {
        response = clusterObj.acceptTiggerStream(triggers, clusterID, clientID);
        TriggerLog.writeTolog(clusterID,triggers);
        if (response != null) {
            Logger.log("Triggers send to the cluster");
        } else {
            Logger.log("Triggers not accepted");
        }
    }

    private static void setClusterObject(Object obj) {
        clusterObj = (ClusterInterface) obj;
    }

}
