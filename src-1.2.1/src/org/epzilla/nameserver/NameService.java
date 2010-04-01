package org.epzilla.nameserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NameService extends Remote {

    public int insertNode(String name, String ipAdrs, int portNumber) throws RemoteException;

    public int getDirectorySize() throws RemoteException;

    //    public String getDispatcher(String clientID) throws RemoteException;
    public String getDispatcherIP() throws RemoteException;

    public void updateLoad(String ip) throws RemoteException;
}

