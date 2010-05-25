package org.epzilla.clusterNode.dataManager;

import org.epzilla.clusterNode.nodeControler.EventSender;
import org.epzilla.util.CircularList;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
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
    private static CircularList<String> lis = new CircularList();

    public EventsManager(String id) {
        this.clientId = id;
        eventQueue = new ConcurrentLinkedQueue<String>();
    }

    public static void dispatchEvents() {
        isInit = true;
        count = 0;

        eventsThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    String event;
                    try {
                        event = eventQueue.poll();
                        if (event != null) {
                            EventSender.sendEvents(lis.next(), event, clientId);
                            removeEvents(event);
                            count++;
                            
                        }


                    } catch (MalformedURLException e) {
                        System.err.println(e);
                    } catch (NotBoundException e) {
                        System.err.println(e);
                    } catch (Exception e) {
                        org.epzilla.util.Logger.error("queue poll returns null", e);
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        count = 0;
        ipArr = ClusterIPManager.getNodeIpList();

        for (String ips : ipArr) {
            lis.add(ips);
        }
        isLoaded = true;
    }
}
