package org.epzilla.accumulator.notificationSys;

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
 * Time: 1:13:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientNotifier {
    private static HashMap clientMap = new HashMap<String, String>();
    private static ClientInterface clientObj;
    private static String response = null;
    private static StringBuilder cIP;
    private static String clientIP;


    public ClientNotifier() {

    }

    private static void sendAlertMsg(String alerts, String clientID) throws RemoteException, MalformedURLException, NotBoundException {
        byte[] notification = alerts.getBytes();

        if (clientMap.containsKey(clientID)) {
            clientObj = (ClientInterface) clientMap.get(clientID);
            response = clientObj.notifyClient(notification);
            if (response != null) {
                Logger.log("Notifications send to the client");
                NotificationManager.setAlertCount();
            } else {
                Logger.log("Notifications not sent");
            }
        } else {
            clientIP = generateClientIP(clientID);
            initClient(clientID, clientIP, "CLIENT");
            clientObj = (ClientInterface) clientMap.get(clientID);
            response = clientObj.notifyClient(notification);
            if (response != null) {
                Logger.log("Notifications send to the client");
                NotificationManager.setAlertCount();
            } else {
                Logger.log("Notifications not sent");
            }
        }
    }


    private static void initClient(String clientID, String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClientInterface obj = (ClientInterface) Naming.lookup(url);
        setClientObject(obj);
        clientMap.put(clientID, obj);

    }

    private static void setClientObject(Object obj) {
        clientObj = (ClientInterface) obj;
    }

    /*
   method to generate clientIP from the clientID
    */

    public static String generateClientIP(String cid) {
        String toInsert = ".";
        String preCharacter = "";
        int period = 3;
        int index = 0;

        cIP = new StringBuilder(cid.length() + toInsert.length() * (cid.length() / period) + 1);
        while (index < cid.length()) {
            cIP.append(preCharacter);
            preCharacter = toInsert;
            cIP.append(cid.substring(index, Math.min(index + period, cid.length())));
            index += period;
        }
        return clientIP.toString();
    }

}
