package org.epzilla.clusterNode.dataManager;

import org.epzilla.clusterNode.nodeControler.EventSender;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: May 7, 2010
 * Time: 3:33:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventsManager {
    private static ArrayList<String> ipArr = new ArrayList<String>();
    private static boolean isLoaded = false;
    private static Thread eventsThread;
    private static String clientId;
    private static ConcurrentLinkedQueue<String> eventQueue;
    private static boolean isInit = false;
    private static int count;

    public EventsManager(String id) {
        this.clientId = id;
        eventQueue = new ConcurrentLinkedQueue<String>();
    }

    public static void dispatchEvents() {
        isInit = true;
        count = 0;
        
        eventsThread = new Thread(new Runnable() {
            public void run() {
                String event;
                try {
                    for (int i = 1; i < ipArr.size(); i++) {
                        event = eventQueue.poll();
                        removeEvents(event);
                        count++;
                        EventSender.sendEvents(ipArr.get(i), event, clientId);
                    }
                    if (count >= 1000) {
                        loadNodesDetails();
                        count = 0;
                    }

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

    public static void addEvents(String events) {
        eventQueue.add(events);
        if (!isInit) {
            loadNodesDetails();
            dispatchEvents();
            eventsThread.start();
        }

    }

    public static void removeEvents(String events) {
        eventQueue.remove(events);

    }

    private static void loadNodesDetails() {
        ipArr = ClusterIPManager.getNodeIpList();
        isLoaded = true;
    }
}
