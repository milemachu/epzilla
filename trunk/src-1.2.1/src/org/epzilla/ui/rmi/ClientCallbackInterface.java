package org.epzilla.ui.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallbackInterface extends Remote {
    public void notifyClient(String message) throws RemoteException;
}
