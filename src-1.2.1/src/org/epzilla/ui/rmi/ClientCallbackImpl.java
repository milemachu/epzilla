package org.epzilla.ui.rmi;

import org.epzilla.ui.controlers.ClientUIControler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

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
