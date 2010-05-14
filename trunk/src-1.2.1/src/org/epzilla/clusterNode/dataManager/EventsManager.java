package org.epzilla.clusterNode.dataManager;

import org.epzilla.clusterNode.nodeControler.EventSender;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
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
    private ConcurrentLinkedQueue eventQueue;

//    public static

    public static void eventsToNodes(final ArrayList<String> eList, final String clientID) {
        if (!isLoaded) {
            loadNodesDetails();
        }
        eventsThread = new Thread(new Runnable() {
            public void run() {
                   try {
                       EventSender eSender = new EventSender(ipArr,clientID,eList);
                       eSender.sendEvents();
                } catch (MalformedURLException e) {
                    System.err.println(e);
                } catch (NotBoundException e) {
                    System.err.println(e);
                } catch (RemoteException e) {
                    System.err.println(e);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void loadNodesDetails() {
        ipArr = ClusterIPManager.getNodeIpList();
        isLoaded = true;
    }
}
