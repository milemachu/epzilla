package org.epzilla.dispatcher.dataManager;

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
    private static ClientInterface clientObj;
    private static String response = null;

    public static void acceptNotifications(String serverIp,String notifications) throws MalformedURLException, NotBoundException, RemoteException {
        if (clientMap.containsKey(serverIp)) {
            clientObj= (ClientInterface) clientMap.get(serverIp);
            sendNotifications(notifications);
        } else {
            initClient(serverIp, "CLIENT");
            sendNotifications(notifications);
        }
    }

    private static void initClient(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClientInterface obj = (ClientInterface) Naming.lookup(url);
        setClientObject(obj);
        clientMap.put(serverIp, clientObj);

    }

    public static void sendNotifications(String list) throws RemoteException {
        response = clientObj.notifyClient(list);
        if (response != null)
            Logger.log("Notifications send to the client");
        else
            Logger.log("Notifications not sent");

    }

    private static void setClientObject(Object obj) {
        clientObj = (ClientInterface) obj;
    }

}
