package org.epzilla.clusterNode.rmi;

import org.epzilla.clusterNode.dataManager.TriggerManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 11, 2010
 * Time: 10:49:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterImpl extends UnicastRemoteObject implements ClusterInterface {

    public ClusterImpl() throws RemoteException {}

    public String acceptTiggerStream(ArrayList<String> tList, String clusterID, String clientID) throws RemoteException {
        try {
            for (int i = 0; i < tList.size(); i++) {
                TriggerManager.addTriggerToList(tList.get(i), clientID);
            }
            return "OK";
        } catch (Exception e) {
            System.out.println("Trigger adding failure");
        }
        return null;
    }

    public String acceptEventStream(ArrayList<String> eList, String clusterID, String clientID) throws RemoteException {
        try {

            //event accepting logic here

            return "OK";
        } catch (Exception e) {
            System.out.println("Events adding failure");
        }
        return null;
    }

    public String deleteTriggers(ArrayList<String> list, String clusterID, String clientID) throws RemoteException {
       // trigger deleting logic here
        return null;
    }
}
