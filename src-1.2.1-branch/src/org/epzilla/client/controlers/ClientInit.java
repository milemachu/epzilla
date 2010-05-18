package org.epzilla.client.controlers;

import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.testObjectGenerator.EventTriggerGenerator;
import org.epzilla.util.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 8, 2010
 * Time: 12:40:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientInit extends Thread {
    private static Object lock = new Object();
    private static String cID = "";
    private static String clientID;
    private static DispInterface di;
    private static Thread trigger;
    private static Thread events;
    private static volatile boolean isLive = false;

    public ClientInit() {
    }

    public static void lookUp(String ip, String name) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + ip + "/" + name;
        DispInterface di = (DispInterface) Naming.lookup(url);
        setDispatcherObj(di);
    }
    public static void initProcess(String ip, String name, String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        lookUp(ip, name);
        ClientInit.clientID = clientID;
        isLive = true;
        initSendTriggerStream();
        initSendEventsStream();
        trigger.start();
        events.start();
    }

    public static void initSendTriggerStream() {
        trigger = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (isLive) {
                    int triggerSeqID = 1;
                    String response = null;

                    ArrayList<String> triggers = new ArrayList<String>();
                    for (int i = 0; i < 500; i++) {
                        triggers.add(EventTriggerGenerator.getNextTrigger());
                    }
                    try {
                        response = di.uploadTriggersToDispatcher(triggers, clientID, triggerSeqID);
                    } catch (RemoteException e) {
                    }

                    if (response != null) {
                        Logger.log("Dispatcher Recieved the triggrs from the client and the response is " + response);
                    } else {
                        ClientUIControler.appendResults("Connection to the Dispatcher service failed, trigger sending stoped" + "\n");
                        return;
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

    public static void initSendEventsStream() {
        events = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (isLive) {
                    int eventsSeqID = 1;
                    String response = null;

                        String event = EventTriggerGenerator.getNextEvent();

                    byte []buffer =  event.getBytes();

                    try {
                        response = di.uploadEventsToDispatcher(buffer, clientID, eventsSeqID);
                    } catch (RemoteException e) {
                    }

                    if (response != null)
                        Logger.log("Dispatcher Recieved the events from the client and the response is " + response);
                    else {
                        ClientUIControler.appendResults("Connection to the Dispatcher service failed, events sending stoped" + "\n");
                        return;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void stopEventTriggerStream() {
        isLive = false;
    }

    private static void setDispatcherObj(Object obj) {
        di = (DispInterface) obj;
    }
}
