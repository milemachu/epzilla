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
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 3, 2010
 * Time: 1:13:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientNotifier {
    private static HashMap clientMap = new HashMap<String, String>();
    private static ArrayList<String> alertBuffer = new ArrayList<String>();
    private static ConcurrentLinkedQueue<String> alertQueue = new ConcurrentLinkedQueue<String>();
    private static Thread runner;
    private static boolean isInit = false;

    public ClientNotifier() {

    }
    /*
    method to get the alert message
    */

    public static void addAlertMessage(String alert, String clientID) {
        alertQueue.add(alert + ":" + clientID);
        if (!isInit) {
            initSendAlert();
            runner.start();
        }

    }
    /*
    method to send alert message
     */

    private static void sendAlertMsg(String alerts, String clientID) throws RemoteException, MalformedURLException, NotBoundException {
        byte[] notification = alerts.getBytes();

        String response = null;
        if (clientMap.containsKey(clientID)) {
            ClientInterface clientObj = (ClientInterface) clientMap.get(clientID);
            response = clientObj.notifyClient(notification);
            if (response != null) {
                Logger.log("Notifications send to the client");
                NotificationManager.setAlertCount();
            } else {
                alertQueue.add(alerts + ":" + clientID);
                Logger.log("Notifications not sent");
            }
        } else {
            String clientIP = generateClientIP(clientID);
            ClientInterface clientObj = initClient(clientID, clientIP, "CLIENT");
            response = clientObj.notifyClient(notification);
            if (response != null) {
                Logger.log("Notifications send to the client");
                NotificationManager.setAlertCount();
            } else {
                alertQueue.add(alerts + ":" + clientID);
                Logger.log("Notifications not sent");
            }
        }
    }

    public static void initSendAlert() {
        isInit  = true;
        runner = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        String msg;
                        msg = alertQueue.poll();
                        if (msg != null) {
                            StringTokenizer st = new StringTokenizer(msg, ":");
                            String alert = st.nextToken();
                            String id = st.nextToken();
                            sendAlertMsg(alert, id);
                            Thread.sleep(1000);
                        }

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();  
                    }
                }
            }
        });
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
        StringBuilder cIP = new StringBuilder(cid.length() + toInsert.length() * (cid.length() / period) + 1);
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
                addAlertMessage("Alert " + i, "127000000001");
                Thread.sleep(100);
            }
            initSendAlert();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
