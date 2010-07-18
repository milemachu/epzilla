package org.epzilla.clusterNode.accConnector;

import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * This class is use to send the Derived events
 * Author: Chathura
 * Date: May 19, 2010
 * Time: 8:35:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeriveEventManager {
    private static ArrayList<String> ipArr = new ArrayList<String>();
    private static boolean isLoad = false;

    public static void dispatchEvents(String event) {
        if (!isLoad) {
            loadAccumulatorIPs();
        }
        byte[] bteEvent = event.getBytes();

        try {
            DeriveEventSender.sendDeriveEvent(ipArr.get(0), bteEvent);
        } catch (MalformedURLException e) {
            Logger.error("", e);
        } catch (NotBoundException e) {
            Logger.error("", e);
        } catch (RemoteException e) {
            Logger.error("", e);
        }
    }

    public static void loadAccumulatorIPs() {
        isLoad = true;
    }
}
