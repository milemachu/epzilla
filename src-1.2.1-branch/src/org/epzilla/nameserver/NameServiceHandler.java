package org.epzilla.nameserver;


import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by IntelliJ IDEA.
 * This is Startup class of the Name Server
 * Author: Chathura
 * To change this template use File | Settings | File Templates.
 */
public class NameServiceHandler extends UnicastRemoteObject {
    private static String serviceName = "NAME_SERVICE";

    public NameServiceHandler() throws RemoteException {
    }
    /*
   start RMI registry
    */

    private void startRegistry() {
        try {
            Runtime.getRuntime().exec("rmiregistry");
            Thread.sleep(1000);
        }
        catch (IOException ex) {
            Logger.error("IO error:", ex);
        }
        catch (InterruptedException exc) {
            Logger.error("Interruption error:", exc);
        }
    }
    /*
    bind the name server to its registry
     */

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
        handler.bind(serviceName);

    }


}

