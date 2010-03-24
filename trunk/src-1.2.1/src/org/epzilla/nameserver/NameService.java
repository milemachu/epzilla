package org.epzilla.nameserver;

import java.rmi.*;
public interface NameService extends Remote {
    public int search(String str) throws RemoteException;
    public int insertNode(String name, String ipAdrs, int portNumber)throws RemoteException;
    public int getPort(int index) throws RemoteException;
    public String getHostName(int index) throws RemoteException;
    public String getNames(int index) throws RemoteException;
    public int getDirectorySize() throws RemoteException;
    public String getDispatcher(String clientID) throws RemoteException;
}

