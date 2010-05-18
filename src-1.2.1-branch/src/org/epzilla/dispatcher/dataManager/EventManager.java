package org.epzilla.dispatcher.dataManager;

import org.epzilla.dispatcher.clusterHandler.EventSender;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: May 2, 2010
 * Time: 4:24:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventManager {
    private static ArrayList<String> ipArr = new ArrayList<String>();
    private static ArrayList<String> idArr = new ArrayList<String>();
    private static boolean isLoaded = false;
    private static Thread eventsThread;

    public static void sendEvents(byte[] event, String clientID) {
//        if (!isLoaded) {
//            loadClusterDetails();
//        }
//        eventsThread = new Thread(new Runnable() {
//            public void run() {
        try {
            ArrayList<String> ips = ClusterLeaderIpListManager.getClusterIpList();
            ArrayList<String> ids = ClusterLeaderIpListManager.getClusterIdList();
            EventSender.acceptEvent(ips, ids, event, clientID);

        } catch (MalformedURLException e) {
            System.err.println(e);
        } catch (NotBoundException e) {
            System.err.println(e);
        } catch (RemoteException e) {
            System.err.println(e);
        }
//                try {
//                    Thread.sleep(200);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

//    private static void loadClusterDetails() {
//
//        ipArr = ClusterLeaderIpListManager.getClusterIpList();
//        idArr = ClusterLeaderIpListManager.getClusterIdList();
//        isLoaded = true;
//    }
}
