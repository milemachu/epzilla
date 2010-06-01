package org.epzilla.accumulator.notificationSys;

import org.epzilla.client.rmi.ClientInterface;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 3, 2010
 * Time: 1:13:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientNotifier {
    private static HashMap clientMap = new HashMap<String, String>();
    private static String response = null;
    private static StringBuilder cIP;
    private static String clientIP;
    private static ArrayList<String> alertBuffer = new ArrayList<String>();

    public ClientNotifier() {

    }

    private static void sendAlertMsg(String alerts, String clientID) throws RemoteException, MalformedURLException, NotBoundException {
        byte[] notification = alerts.getBytes();

        if (clientMap.containsKey(clientID)) {
            ClientInterface clientObj = (ClientInterface) clientMap.get(clientID);
            response = clientObj.notifyClient(notification);
            if (response != null) {
                Logger.log("Notifications send to the client");
                NotificationManager.setAlertCount();
            } else {
                alertBuffer.add(alerts + ":" + clientID);
                Logger.log("Notifications not sent");
            }
        } else {
            clientIP = generateClientIP(clientID);
            ClientInterface clientObj = initClient(clientID, clientIP, "CLIENT");
            response = clientObj.notifyClient(notification);
            if (response != null) {
                Logger.log("Notifications send to the client");
                NotificationManager.setAlertCount();
            } else {
                alertBuffer.add(alerts + ":" + clientID);
                Logger.log("Notifications not sent");
            }
        }
    }

    public static void resendAlerts() throws MalformedURLException, NotBoundException, RemoteException {
        for (String buffer : alertBuffer) {
            StringTokenizer st = new StringTokenizer(buffer, ":");
            String alert = st.nextToken();
            String id = st.nextToken();
            sendAlertMsg(alert, id);
        }
    }

    private static ClientInterface initClient(String clientID, String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        ClientInterface obj = (ClientInterface) Naming.lookup(url);
        clientMap.put(clientID, obj);
        return obj;

    }
    /*
   method to generate clientIP from the clientID
    */

    public static String generateClientIP(String cid) {
        String toInsert = ".";
        String preCharacter = "";
        int period = 3;
        int index = 0;
        String clientIp = "";
        cIP = new StringBuilder(cid.length() + toInsert.length() * (cid.length() / period) + 1);
        while (index < cid.length()) {
            cIP.append(preCharacter);
            preCharacter = toInsert;
            cIP.append(cid.substring(index, Math.min(index + period, cid.length())));
            index += period;
        }
        clientIp = cIP.toString();
        return clientIp;
    }
    //method for testing the notification system

    public static void main(String[] args) {
        try {
            for (int i = 0; i < 10; i++) {
                sendAlertMsg("hello", "127000000001");
                Thread.sleep(2000);
            }
            resendAlerts();
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NotBoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
