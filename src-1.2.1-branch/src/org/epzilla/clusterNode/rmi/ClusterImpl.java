package org.epzilla.clusterNode.rmi;

import org.epzilla.clusterNode.dataManager.EventsManager;
import org.epzilla.clusterNode.dataManager.TriggerManager;
import org.epzilla.util.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 11, 2010
 * Modified: May 11, 2010
 * Time: 10:49:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterImpl extends UnicastRemoteObject implements ClusterInterface {

    public ClusterImpl() throws RemoteException {
    }

    /*
   Accept trigger stream by Leader node
    */
    public String acceptTiggerStream(ArrayList<String> tList, String clusterID, String clientID) throws RemoteException {
        try {
            for (int i = 0; i < tList.size(); i++) {
                TriggerManager.addTriggerToList(tList.get(i), clientID);
            }
            return "OK";
        } catch (Exception e) {
            System.err.println("Trigger adding failure");
        }
        return null;
    }

    public String acceptEventStream(byte[] event, String clusterID, String clientID) throws RemoteException {
        try {
            EventsManager em = new EventsManager(clientID);
            String eventS = new String(event);
            EventsManager.addEvents(eventS);
            Logger.log(eventS);


            return "OK";
        } catch (Exception e) {
            Logger.log("Events adding failure");
        }
        return null;
    }

    public String addEventStream(ArrayList<String> eList, String clientID) throws RemoteException {
        return null;
    }

    /*
   Accept trigger stream by Processing Node
    */
    public String addTriggerStream(ArrayList<String> tlist, String clientID) throws RemoteException {
        return null;
    }

    public String deleteTriggers(ArrayList<String> list, String clusterID, String clientID) throws RemoteException {
        // trigger deleting logic here
        return null;
    }
}
