package org.epzilla.clusterNode.replayLogs;

import org.epzilla.clusterNode.xml.ClusterSettingsReader;
import org.epzilla.dispatcher.rmi.DispInterface;

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
 * Date: Apr 20, 2010
 * Time: 11:13:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReplayLog {
    private static ClusterSettingsReader reader = new ClusterSettingsReader();
    private static String clusterID = "";

    private static void replayLog() throws RemoteException, MalformedURLException, NotBoundException, UnknownHostException {
        loadSettings();
        String url = "rmi://" + "127.0.0.1" + "/" + "Dispatcher127000000001";
        DispInterface service;
        service = (DispInterface) Naming.lookup(url);
        InetAddress inetAddress = InetAddress.getLocalHost();
        String ipAddress = inetAddress.getHostAddress();
        service.replayLogs(clusterID, ipAddress);
    }

    private static void loadSettings() {
        try {
            ArrayList<String[]> data = reader.getServerIPSettings("./src/settings/clusterID_settings.xml");
            String[] ar = data.get(0);
            clusterID = ar[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
