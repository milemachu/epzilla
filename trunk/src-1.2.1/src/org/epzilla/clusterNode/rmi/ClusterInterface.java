package org.epzilla.clusterNode.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 11, 2010
 * Time: 10:44:48 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ClusterInterface extends Remote {
    public String acceptTiggerStream(ArrayList<String> tList, String cID) throws RemoteException;

    public String acceptEventStream(ArrayList<String> eList, String cID) throws RemoteException;
}
