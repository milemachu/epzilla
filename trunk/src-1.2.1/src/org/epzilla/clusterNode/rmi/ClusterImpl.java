package org.epzilla.clusterNode.rmi;

import org.epzilla.clusterNode.dataManager.TriggerManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 11, 2010
 * Time: 10:49:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterImpl extends UnicastRemoteObject implements ClusterInterface {

    public ClusterImpl() throws RemoteException{

    }

    public void acceptTiggerStream(byte[] stream, String cID, int triggerSeqID) throws RemoteException {
        try {
            TriggerManager.addTriggerToList(stream);
        } catch (Exception e) {
                     e.printStackTrace();
        }
    }

    public void acceptEventStream(byte[] stream, String cID, int eventSeqID) throws RemoteException {

    }
}
