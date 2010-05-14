package org.epzilla.clusterNode.dataManager;

import org.epzilla.clusterNode.nodeControler.EventSender;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.nio.Buffer;

/**
 * Created by IntelliJ IDEA.
 * User: chathura
 * Date: May 7, 2010
 * Time: 3:33:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventsManager {
    private static ArrayList<String> ipArr = new ArrayList<String>();
    private static ArrayList<String> eList = new ArrayList<String>();
    private static boolean isLoaded = false;
    private static Thread eventsThread;
    private static String clientId;
    private static ConcurrentLinkedQueue<String> eventQueue;

    public EventsManager(String id){
        this.clientId = id;
        eventQueue = new ConcurrentLinkedQueue<String>();
    }

    public static void eventsToNodes() {
        if (!isLoaded) {
            loadNodesDetails();
        }
        eventsThread = new Thread(new Runnable() {
            public void run() {
                String event;
                   try {
                       for(int i=0;i<20;i++){
                           event = eventQueue.poll();
                           eList.add(event);
                           removeEvents(event);
                       }
                       EventSender eSender = new EventSender(ipArr,clientId,eList);
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

    public static void addEvents(String events){
        eventQueue.add(events);
    
    }
    public static void removeEvents(String events){
        eventQueue.remove(events);

    }
    private static void loadNodesDetails() {
        ipArr = ClusterIPManager.getNodeIpList();
        isLoaded = true;
    }
}
