package org.epzilla.dispatcher.clusterHandler;

import org.epzilla.clusterNode.rmi.ClusterInterface;
import org.epzilla.dispatcher.rmi.TriggerRepresentation;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * This class is to send triggers to the Cluster leaders
 * Author: Chathura
 * Date: Mar 11, 2010
 * Time: 1:17:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TriggerSender {
    private static HashMap idMap = new HashMap<String, String>();
    private static String serviceName = "CLUSTER_NODE";
    private static String response = null;
    private static TriggerSender instance = new TriggerSender();

    public TriggerSender() {
    }

    public static TriggerSender getInstance() {
        return instance;
    }

    /**
     * add triggers to the Cluster leaders
     * @param serverIp
     * @param clusterID
     * @param triggers
     * @param clientID
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
        public static void triggerAdding(String serverIp, String clusterID, ArrayList<TriggerRepresentation> triggers, String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        if (idMap.containsKey(serverIp)) {
            ClusterInterface clusterObj = (ClusterInterface) idMap.get(serverIp);
            sendTriggers(clusterObj, triggers, clusterID, clientID);
        } else {
            ClusterInterface clusterObj = initCluster(serverIp, serviceName);
            sendTriggers(clusterObj, triggers, clusterID, clientID);
        }

    }

    /**
     * requesting trigger deletion
     * @param serverIp
     * @param clusterID
     * @param triggers
     * @param clientID
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    public static void requestTriggerDeletion(String serverIp, String clusterID, ArrayList<TriggerRepresentation> triggers, String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        if (idMap.containsKey(serverIp)) {
            ClusterInterface clusterObj = (ClusterInterface) idMap.get(serverIp);
            deleteTriggers(clusterObj, triggers, clusterID, clientID);
        } else {
            ClusterInterface clusterObj = initCluster(serverIp, serviceName);
            deleteTriggers(clusterObj, triggers, clusterID, clientID);
        }

    }

    /**
     *   initialize the remote reference to the cluster interface
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
        idMap.put(serverIp, obj);
        return obj;

    }

    /**
     *  send triggers to the cluster leaders
     * @param co
     * @param triggers
     * @param clusterID
     * @param clientID
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    private static void sendTriggers(ClusterInterface co, ArrayList<TriggerRepresentation> triggers, String clusterID, String clientID) throws RemoteException, MalformedURLException, NotBoundException {
        String response = co.acceptTiggerStream(triggers);
        TriggerLog.writeTolog(clientID, clusterID, triggers);
        if (response != null) {
            Logger.log("Triggers send to the cluster");
        } else {
            Logger.log("Triggers not accepted");
        }

    }

    /**
     * delete triggers from the clusters
     * @param co
     * @param triggers
     * @param clusterID
     * @param clientID
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    private static void deleteTriggers(ClusterInterface co, ArrayList<TriggerRepresentation> triggers, String clusterID, String clientID) throws RemoteException, MalformedURLException, NotBoundException {

        co.deleteTriggers(triggers, clusterID, clientID);
        if (response != null) {
            Logger.log("Triggers to be deleted sent to the cluster");
        } else {
            Logger.log("Triggers to be deleted - not accepted");
        }

    }


}
