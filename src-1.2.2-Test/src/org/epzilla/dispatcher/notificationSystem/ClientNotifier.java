package org.epzilla.dispatcher.notificationSystem;

import org.epzilla.client.rmi.ClientInterface;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 3, 2010
 * Time: 3:27:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientNotifier {
    private static HashMap clientMap = new HashMap<String, String>();
    private static String response = null;

    public static void getNotifications(String serverIp, String notifications) throws MalformedURLException, NotBoundException, RemoteException {
        byte[] msg = notifications.getBytes();

        if (clientMap.containsKey(serverIp)) {
            ClientInterface clientObj = (ClientInterface) clientMap.get(serverIp);
            response = clientObj.notifyClient(msg);
            if (response != null)
                Logger.log("Notifications send to the client");
            else
                Logger.log("Notifications not sent");
        } else {
            ClientInterface clientObj = initClient(serverIp, "CLIENT");
            response = clientObj.notifyClient(msg);
            if (response != null)
                Logger.log("Notifications send to the client");
            else
                Logger.log("Notifications not sent");
        }
    }

    private static ClientInterface initClient(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClientInterface obj = (ClientInterface) Naming.lookup(url);
        clientMap.put(serverIp, obj);
        return obj;

    }

}
