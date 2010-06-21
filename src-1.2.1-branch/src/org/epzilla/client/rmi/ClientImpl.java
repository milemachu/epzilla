package org.epzilla.client.rmi;

import org.epzilla.client.controlers.ClientUIControler;
import org.epzilla.util.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 3, 2010
 * Time: 4:20:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientImpl extends UnicastRemoteObject implements ClientInterface {


    public ClientImpl() throws RemoteException {
    }

    /*
   accept alert messages
    */

    public String notifyClient(byte[] notifications) {
        try {
            String alert = new String(notifications);
            ClientUIControler.appendAlerts(alert);
//            ClientUIControler clientCon = new ClientUIControler(alert);
//            clientCon.setAlertCount();
//            Thread t = new Thread(clientCon);
//            t.start();
            return "OK";
        } catch (Exception ex) {
            Logger.error("", ex);
        }
        return null;
    }
}
