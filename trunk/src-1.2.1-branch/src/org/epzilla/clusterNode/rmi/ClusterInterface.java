package org.epzilla.clusterNode.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 11, 2010
 * Modified: May 11, 2010
 * Time: 10:44:48 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClusterInterface extends Remote {
    public String acceptTiggerStream(ArrayList<String> tList, String clusterID, String clientID) throws RemoteException;

    public String acceptEventStream(byte[] event, String clusterID, String clientID) throws RemoteException;

    public String addEventStream(ArrayList<String> eList, String clientID) throws RemoteException;

    public String addTriggerStream(ArrayList<String> tlist, String clientID) throws RemoteException;

    public String deleteTriggers(ArrayList<String> list, String clusterID, String clientID) throws RemoteException;
}
