package org.epzilla.ui.controlers;

import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.testObjectGenerator.EventTriggerGenerator;

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
    private static String cID;
    private static DispInterface di;
    private static Thread trigger;
    private static Thread events;

    public ClientInit() {
    }

    public static void lookUp(String ip, String name) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + ip + "/" + name;
        DispInterface di = (DispInterface) Naming.lookup(url);
        setDispatcherObj(di);
    }

    public static void initProcess(String ip, String name,String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        lookUp(ip, name);
        cID = clientID;
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
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    while (true) {
                        int triggerSeqID = 1;
                        String response = null;

                        ArrayList<String> triggers = new ArrayList<String>();
                        for (int i = 0; i < 10; i++) {
                            triggers.add(EventTriggerGenerator.getNextTrigger());
                        }
                        try {
                            response = di.uploadTriggersToDispatcher(triggers, cID, triggerSeqID);
                        } catch (RemoteException e) {
                        }

                        if (response != null) {
                            System.out.println("Dispatcher Recieved the triggrs from the client and the response is " + response);
                        } else {
                            ClientUIControler.appendResults("Dispatcher service not working or connection to the Dispatcher service failed" + "\n");
                            return;
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    while (true) {
                        int eventsSeqID = 1;
                        String response = null;

                        ArrayList<String> events = new ArrayList<String>();
                        for (int i = 0; i < 10; i++) {
                            events.add(EventTriggerGenerator.getNextEvent());
                        }

                        try {
                            response = di.uploadEventsToDispatcher(events, cID, eventsSeqID);
                        } catch (RemoteException e) {
                        }

                        if (response != null)
                            System.out.println("Dispatcher Recieved the events from the client and the response is " + response);
                        else {
                            ClientUIControler.appendResults("Dispatcher service not working or connection to the Dispatcher service failed" + "\n");
                            return;
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
            });
    }

    public static void stopEventTriggerStream() {
        trigger.interrupt();
        events.interrupt();
    }

    private static void setDispatcherObj(Object obj) {
        di = (DispInterface) obj;
    }

    private static Object getDispatcherObj() {
        return di;
    }
}
