package org.epzilla.client.controlers;

import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.testObjectGenerator.EventTriggerGenerator;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 8, 2010
 * Time: 12:40:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientInit extends Thread {
    private static String clientID;
    private static DispInterface di;
    private static Thread trigger;
    private static Thread events;
    private static HashMap<String, Object> dispMap = new HashMap<String, Object>();
    private static volatile boolean isLive = true;
    private static int eventsSeqID = 1;
    private static boolean dynamicLookup = false;

    public ClientInit() {
    }

    public static void lookUp(String ip, String name) throws MalformedURLException, NotBoundException, RemoteException {
        if (!dispMap.containsKey(ip)) {
            String url = "rmi://" + ip + "/" + name;
            DispInterface di = (DispInterface) Naming.lookup(url);
            setDispatcherObj(di);
            dispMap.put(ip, getDispatcherObj());
        }
    }

    public static void initSend(String ip, String name, String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        lookUp(ip, name);
        ClientInit.clientID = clientID;
        isLive = true;
        initSendTriggerStream(ip);
        initSendEventsStream(ip);
        trigger.start();
        events.start();

    }

    public static void initSendTriggerStream(final String ip) {
        trigger = new Thread(new Runnable() {
            String response = null;
            int triggerSeqID = 1;

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                di = (DispInterface) dispMap.get(ip);

                while (isLive) {
                    ArrayList<String> triggers = new ArrayList<String>();
                    for (int i = 0; i < 200; i++) {
                        triggers.add(EventTriggerGenerator.getNextTrigger());
                    }
                    try {
                        response = di.uploadTriggersToDispatcher(triggers, clientID, triggerSeqID);
                    } catch (RemoteException e) {
                        ClientUIControler.appendResults("Connection to the Dispatcher service failed, trigger sending stoped, Perform Lookup operation..." + "\n");
                        isLive = false;
                        if (!dynamicLookup) {
                            dynamicLookup = true;
                            initDLookup();
                        }

                    }

                    if (response != null) {
                        Logger.log("Dispatcher Recieved the triggrs from the client and the response is " + response);
                    } else {
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void initSendEventsStream(final String ip) {
        events = new Thread(new Runnable() {

            String response;

            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                di = (DispInterface) dispMap.get(ip);

                while (isLive) {
                    String event = EventTriggerGenerator.getNextEvent() + "," + clientID + "," + eventsSeqID;
                    byte[] buffer = event.getBytes();

                    try {
                        response = di.uploadEventsToDispatcher(buffer, clientID, eventsSeqID);
                        eventsSeqID++;
                    } catch (RemoteException e) {
                        isLive = false;
                        ClientUIControler.appendResults("Connection to the Dispatcher service failed, events sending stoped, Perform Lookup operation.." + "\n");
                        if (!dynamicLookup) {
                            dynamicLookup = true;
                            initDLookup();
                        }
                    }

                    if (response != null) {
                        Logger.log("Dispatcher Recieved the events from the client and the response is " + response);
                    } else {
                    }
                }
            }
        });
    }

    private static void initDLookup() {
        if (dynamicLookup) {
            DynamicLookup.dynamicLookup();
        }
    }

    public static void stopEventTriggerStream() {
        isLive = false;
    }

    private static void setDispatcherObj(Object obj) {
        di = (DispInterface) obj;
    }

    private static Object getDispatcherObj() {
        return di;
    }
}
