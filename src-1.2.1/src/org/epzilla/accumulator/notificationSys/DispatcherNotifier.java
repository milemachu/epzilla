package org.epzilla.accumulator.notificationSys;

import org.epzilla.dispatcher.rmi.DispInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Chathura
 * Date: May 3, 2010
 * Time: 12:54:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatcherNotifier {
    private static DispInterface dispObj;
    private static String response = null;

    public static void getAlerts(String dispIP, String serviceName, ArrayList<String> alerts,String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        initDispatcher(dispIP, serviceName);
        sendAlerts(alerts,clientID);
    }

    private static void initDispatcher(String serverIp, String serviceName) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + serverIp + "/" + serviceName;
        DispInterface obj = (DispInterface) Naming.lookup(url);
        setDispatcherObject(obj);

    }

    private static void sendAlerts(ArrayList<String> alerts,String clientID) throws RemoteException, MalformedURLException, NotBoundException {
        dispObj.acceptNotifications(alerts,clientID);
        if (response != null)
            System.out.println("Alert message send to the Dispatcher");
        else
            System.out.println("Alert message sending failed");
    }

    private static void setDispatcherObject(Object obj) {
        dispObj = (DispInterface) obj;
    }
}
