package org.epzilla.accumulator.notificationSys;

import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * This class use to send accumulated notifications to the Dispatcher..
 * Author: Chathura
 * Date: May 3, 2010
 * Time: 1:14:36 PM
 */
public class DispatcherNotifier {
    private static DispInterface dispObj;
    private static String response = null;

    public static void getAlerts(String dispIP, String serviceName, String alerts,String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        initDispatcher(dispIP, serviceName);
        sendAlerts(alerts,clientID);
    }

    private static void initDispatcher(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        DispInterface obj = (DispInterface) Naming.lookup(url);
        setDispatcherObject(obj);

    }

    private static void sendAlerts(String alerts,String clientID) throws RemoteException, MalformedURLException, NotBoundException {
        dispObj.getNotifications(alerts,clientID);
        if (response != null)
            Logger.log("Alert message send to the Dispatcher");
        else
            Logger.log("Alert message sending failed");
    }

    private static void setDispatcherObject(Object obj) {
        dispObj = (DispInterface) obj;
    }
}
