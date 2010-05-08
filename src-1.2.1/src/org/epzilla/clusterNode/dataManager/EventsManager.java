package org.epzilla.clusterNode.dataManager;

import org.epzilla.dispatcher.clusterHandler.EventSender;
import org.epzilla.dispatcher.dataManager.ClusterLeaderIpListManager;

import java.util.ArrayList;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: May 7, 2010
 * Time: 3:33:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventsManager {
    private static ArrayList<String> ipArr = new ArrayList<String>();
    private static ArrayList<String> idArr = new ArrayList<String>();
    private static boolean isLoaded = false;
    private static Thread eventsThread;

    public static void eventsToNodes(final ArrayList<String> eList, final String clientID) {
        if (!isLoaded) {
            loadNodesDetails();
        }
        eventsThread = new Thread(new Runnable() {
            public void run() {
                 // events sending logic here
            }
        });
    }

    private static void loadNodesDetails() {
          isLoaded = true;
    }
}
