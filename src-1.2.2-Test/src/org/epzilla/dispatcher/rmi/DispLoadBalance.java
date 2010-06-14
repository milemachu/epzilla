package org.epzilla.dispatcher.rmi;

import org.epzilla.dispatcher.xml.ServerSettingsReader;
import org.epzilla.nameserver.NameService;

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
 * User: chathura
 * Date: Mar 29, 2010
 * Time: 10:31:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispLoadBalance {
    private static ServerSettingsReader reader = new ServerSettingsReader();
    private static String ip = "";
    private static String port = "";
    private static String serviceName = "";

    public static void updateIncLoad() throws MalformedURLException, RemoteException, NotBoundException, UnknownHostException {
        loadSettings();
        String url = "rmi://" + ip + "/" + serviceName;
        NameService service = (NameService) Naming.lookup(url);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();
        service.updateIncLoad(ipAddress);
    }

    public static void updateDecLoad() throws MalformedURLException, NotBoundException, RemoteException, UnknownHostException {
        String url = "rmi://" + ip + "/" + serviceName;
        NameService service = (NameService) Naming.lookup(url);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();
        service.updateDecLoad(ipAddress);
    }

    private static void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("./src/settings/server_settings.xml");
            String[] ar = data.get(0);
            ip = ar[0];
            port = ar[1];
            serviceName = ar[2];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
