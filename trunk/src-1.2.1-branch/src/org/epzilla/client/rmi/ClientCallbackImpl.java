package org.epzilla.client.rmi;

import org.epzilla.client.controlers.ClientUIControler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
/**
 * Created by IntelliJ IDEA.
 * This class use to send callback information to the client
 * Author: Chathura
 * Date: Mar 2, 2010
 * Time: 12:40:41 PM
 */
public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallbackInterface {

    private static ClientUIControler clientCon;

    public ClientCallbackImpl() throws RemoteException {
        super();
    }

    public void notifyClient(String message) {
        clientCon = new ClientUIControler(message);
        Thread t = new Thread(clientCon);
        t.start();
    }
}
