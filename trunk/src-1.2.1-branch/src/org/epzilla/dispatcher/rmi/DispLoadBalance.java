package org.epzilla.dispatcher.rmi;

import org.epzilla.dispatcher.xml.ServerSettingsReader;
import org.epzilla.nameserver.NameService;
import org.epzilla.util.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * This is the Implementation of Dispatcher Load balance class
 * This will update the Dispatcher load at Name Server when a client i connected to the Dispatcher
 * Author: Chathura
 * Date: Mar 29, 2010
 * Time: 10:31:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispLoadBalance {
    private static ServerSettingsReader reader = new ServerSettingsReader();
    private static String ip = "";
    private static String port = "";
    private static String serviceName = "";

    /**
     * update the load of Dispatcher by calling to the Name Server, when a client is connected
     *
     * @throws MalformedURLException
     * @throws RemoteException
     * @throws NotBoundException
     * @throws UnknownHostException
     */
    public static void updateIncLoad() throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException {
        loadSettings();
        String url = "rmi://" + ip + "/" + serviceName;
        NameService service = (NameService) Naming.lookup(url);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();
        service.updateIncLoad(ipAddress);
    }

    /**
     * update the load of Dispatcher by calling to the Name Server when a client is unregister from the Dispatcher
     *
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     * @throws UnknownHostException
     */
    public static void updateDecLoad() throws MalformedURLException, NotBoundException, RemoteException, UnknownHostException {
        String url = "rmi://" + ip + "/" + serviceName;
        NameService service = (NameService) Naming.lookup(url);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();
        service.updateDecLoad(ipAddress);
    }
    /*
   read server_settings XML file and get the Name Server details
    */

    private static void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("server_settings.xml");
            String[] ar = data.get(0);
            ip = ar[0];
            port = ar[1];
            serviceName = ar[2];
        } catch (IOException e) {
            Logger.error("", e);
        }
    }
}
