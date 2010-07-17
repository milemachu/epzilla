package org.epzilla.client.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * Created by IntelliJ IDEA.
 * Interface class
 * Author: Chathura
 * Date: Mar 2, 2010
 * Time: 12:40:41 PM
 */
public interface ClientCallbackInterface extends Remote {
    public void notifyClient(String message) throws RemoteException;
}
