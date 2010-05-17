package org.epzilla.client.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 3, 2010
 * Time: 4:20:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClientInterface extends Remote {

    public String notifyClient(String notifications) throws RemoteException;

}
