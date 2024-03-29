package org.epzilla.nameserver;


import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NameServiceHandler extends UnicastRemoteObject {

    public NameServiceHandler() throws RemoteException {
    }

    private static void startRegistry() {
        Process rmiProcess = null;
        try {
            rmiProcess = Runtime.getRuntime().exec("rmiregistry");
            Thread.sleep(1000);
        }
        catch (IOException ex) {
        }
        catch (InterruptedException exc) {
        }
    }

    public static void bind(String serviceName) {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new org.epzilla.dispatcher.rmi.OpenSecurityManager());
        }
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String url = "rmi://" + ipAddress + "/" + serviceName;
            NameService obj = new NameServiceImpl();
            Naming.rebind(url, obj);
            System.out.println("NameServer successfully deployed");
        } catch (Exception e) {
            System.out.println("NameService err: " + e.getMessage());
        }

    }

    public static void main(String args[]) throws RemoteException {
        NameServiceHandler handler = new NameServiceHandler();
        handler.startRegistry();
        handler.bind("NAME_SERVICE");

    }


}

