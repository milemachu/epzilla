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
 * This class use to send accumulated notifications to the client machine.
 * Author: Chathura
 * Date: May 3, 2010
 * Time: 1:13:36 PM
 */
public class ClientNotifier {
    private static HashMap clientMap = new HashMap<String, String>();
    private static ArrayList<String> alertBuffer = new ArrayList<String>();
    private static ConcurrentLinkedQueue<String> alertQueue = new ConcurrentLinkedQueue<String>();
    private static Thread runner;
    private static boolean isInit = false;
    private static int INITIAL_TIME_INTERVAL = 0;
    private static int UPDATE_TIME_INTERVAL = 10000;
    private static String serviceName = "CLIENT";

    public ClientNotifier() {

    }
    /*
    method to get the alert message
    */

    public static void addAlertMessage(String alert) {
        alertQueue.add(alert);
        if (!isInit) {
            initSendAlert();
            runner.start();
        }

    }
    /*
    method to send alert message
     */

    private static void sendAlertMsg(String alerts, String clientID, String eventId) throws RemoteException, MalformedURLException, NotBoundException {
        byte[] notification = alerts.getBytes();

        String response = null;
        if (clientMap.containsKey(clientID)) {
            ClientInterface clientObj = (ClientInterface) clientMap.get(clientID);
            response = clientObj.notifyClient(notification, eventId.getBytes());
            if (response != null) {
                Logger.log("Notifications send to the client");
            } else {
                alertQueue.add(alerts + ":" + clientID);
                Logger.log("Notifications not sent");
            }
        } else {
            String clientIP = generateClientIP(clientID);
            ClientInterface clientObj = initClient(clientID, clientIP, serviceName);
            response = clientObj.notifyClient(notification, eventId.getBytes());
            if (response != null) {
                Logger.log("Notifications send to the client");
            } else {
                alertQueue.add(alerts + ":" + clientID);
                Logger.log("Notifications not sent");
            }
        }
    }

    public static void initSendAlert() {
        isInit = true;
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
                            String eventId = st.nextToken();
                            sendAlertMsg(alert, id, eventId);
                            Thread.sleep(INITIAL_TIME_INTERVAL, UPDATE_TIME_INTERVAL);
                        }

                    } catch (RemoteException e) {
                        Logger.error("", e);
                    } catch (MalformedURLException e) {
                        Logger.error("", e);
                    } catch (NotBoundException e) {
                        Logger.error("", e);
                    } catch (InterruptedException e) {
                        Logger.error("", e);
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
}
