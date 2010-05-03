package org.epzilla.client.rmi;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 3, 2010
 * Time: 4:23:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientRegister {
    private static String serviceName = "CLIENT";

    public static void bindClient() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new org.epzilla.client.rmi.OpenSecurityManager());
        }
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String url = "rmi://" + ipAddress + "/" + serviceName;
            ClientInterface obj = new ClientImpl();
            Naming.rebind(url, obj);
            System.out.println("Client successfully deployed");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void main(String[] args) {
        bindClient();
    }
}
