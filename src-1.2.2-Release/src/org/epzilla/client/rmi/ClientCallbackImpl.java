package org.epzilla.client.rmi;

import org.epzilla.client.controlers.ClientUIControler;

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
