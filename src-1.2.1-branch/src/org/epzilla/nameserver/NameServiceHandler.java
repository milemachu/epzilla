package org.epzilla.nameserver;


import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NameServiceHandler extends UnicastRemoteObject {

    public NameServiceHandler() throws RemoteException {
    }

    private void startRegistry() {
        try {
            Runtime.getRuntime().exec("rmiregistry");
            Thread.sleep(1000);
        }
        catch (IOException ex) {
        }
        catch (InterruptedException exc) {
        }
    }

    public void bind(String serviceName) {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new org.epzilla.dispatcher.rmi.OpenSecurityManager());
        }
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String url = "rmi://" + ipAddress + "/" + serviceName;
            NameService obj = new NameServiceImpl();
            Naming.rebind(url, obj);
            Logger.log("NameServer successfully deployed");
        } catch (Exception e) {
            Logger.log("NameService err: " + e.getMessage());
        }

    }

    public static void main(String args[]) throws RemoteException {
        NameServiceHandler handler = new NameServiceHandler();
        handler.startRegistry();
        handler.bind("NAME_SERVICE");

    }


}

