package org.epzilla.nameserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NameService extends Remote {

    public int insertNode(String name, String ipAdrs, int portNumber) throws RemoteException;

    public int getDirectorySize() throws RemoteException;

    public String getDispatcherIP() throws RemoteException;

    public String getClientID(String ipAdrs) throws RemoteException;

    public void updateIncLoad(String ip) throws RemoteException;

    public void updateDecLoad(String ip) throws RemoteException;
}

