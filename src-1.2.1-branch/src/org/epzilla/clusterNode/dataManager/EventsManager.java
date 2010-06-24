package org.epzilla.clusterNode.dataManager;

import org.epzilla.clusterNode.nodeControler.EventSender;
import org.epzilla.util.Logger;
import org.epzilla.util.RoundRobinList;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.TimerTask;
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
    private static ConcurrentLinkedQueue<String> eventQueue = new ConcurrentLinkedQueue<String>();
    private static boolean isInit = false;
    private static int count;
    private static RoundRobinList<String> lis = new RoundRobinList();
    private static int UPDATE_SERVICE_RUNNING_TIME = 6000;
    private static int INITIAL_START_TIME = 1000;
    private static int DISPATCH_INIT_TIME = 0;
    private static int DISPATCH_UPDATE_TIME = 1000;
    static java.util.Timer timer = new java.util.Timer();

    public EventsManager() {
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
                            EventSender.sendEvents(lis.next(), event);
//                            removeEvents(event);
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
                        Thread.sleep(DISPATCH_INIT_TIME, DISPATCH_UPDATE_TIME);
                    } catch (InterruptedException e) {
                        Logger.error("", e);
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

    public static void loadNodesDetails() {
        count = 0;
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                ipArr = ClusterIPManager.getNodeIpList();
                lis.clear();
                for (String ips : ipArr) {
//                    if (!lis.contains(ips)) {
                        lis.add(ips);
//                    }
                }
                System.out.println("loaded cluster ip list:" + lis.size());
            }
        }, INITIAL_START_TIME, UPDATE_SERVICE_RUNNING_TIME);

        isLoaded = true;
    }
}
