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

    public ClusterImpl() throws RemoteException {

    }

    public String acceptTiggerStream(ArrayList<String> tList, String cID) throws RemoteException {
        try {
            for (int i = 0; i < tList.size(); i++) {
                TriggerManager.addTriggerToList(tList.get(i));
            }
            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String acceptEventStream(ArrayList<String> eList, String cID) throws RemoteException {
        return null;
    }

    public String deleteTriggers(ArrayList<String> list, String cID) throws RemoteException {
        return null;  
    }
}
