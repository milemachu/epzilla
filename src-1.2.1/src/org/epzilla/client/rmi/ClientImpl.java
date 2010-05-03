package org.epzilla.client.rmi;

import org.epzilla.client.controlers.ClientUIControler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 3, 2010
 * Time: 4:20:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientImpl extends UnicastRemoteObject implements ClientInterface {
    private static ClientUIControler clientCon;

    public ClientImpl() throws RemoteException {
    }

    public String notifyClient(ArrayList<String> notifications) {
        clientCon = new ClientUIControler();
        Thread t = new Thread(clientCon);
        t.start();
        return "OK";
    }
}
