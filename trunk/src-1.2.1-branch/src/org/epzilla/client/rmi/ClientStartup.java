package org.epzilla.client.rmi;

import org.epzilla.client.userInterface.SplashScreen;
import org.epzilla.util.Logger;

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
public class ClientStartup {
    private static String serviceName = "CLIENT";
    static ClientInterface obj;

    public static void bindClient() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new org.epzilla.client.rmi.OpenSecurityManager());
        }
        startRegistry();
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            String url = "rmi://" + ipAddress + "/" + serviceName;
            obj = new ClientImpl();
            Naming.rebind(url, obj);
            Logger.log("Client successfully deployed");
        } catch (Exception e) {
            Logger.error("Client Start up:",e);
        }
    }

    private static void startRegistry() {
        try {
            Runtime.getRuntime().exec("rmiregistry");
            Thread.sleep(1000);
        }
        catch (IOException ex) {
            Logger.error("RMI registry start:",ex);
        }
        catch (InterruptedException exc) {
            Logger.error("RMI registry start: ",exc);
        }
    }

    public static void main(String[] args) {
        bindClient();
        SplashScreen sc = new SplashScreen(3000);
        sc.showSplashAndExit();
    }
}
