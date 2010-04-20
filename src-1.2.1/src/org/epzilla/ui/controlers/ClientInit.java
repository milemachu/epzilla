package org.epzilla.ui.controlers;

import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.testObjectGenerator.EventTriggerGenerator;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by IntelliJ IDEA.
 * User: Team epZilla
 * Date: Mar 8, 2010
 * Time: 12:40:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientInit extends Thread {
    Object lock = new Object();
    DispInterface di;
    static Thread trigger;
    static Thread events;

    public ClientInit() {

    }

    public void lookUp(String ip, String name) throws MalformedURLException, NotBoundException, RemoteException {
        String url = "rmi://" + ip + "/" + name;
        DispInterface di = (DispInterface) Naming.lookup(url);
        setDispatcherObj(di);
    }

    public void initProcess(String ip, String name) throws MalformedURLException, NotBoundException, RemoteException {
        lookUp(ip, name);
        initSendTriggerStream();
//        initSendEventsStream();
        trigger.start();
//        events.start();
    }

    public void initSendTriggerStream() {
        trigger = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                while (true) {
//                synchronized (lock) {

                    String str = EventTriggerGenerator.getNextTrigger();
                    String cID = "1";
                    int triggerSeqID = 1;
                    String response = null;

                    byte[] buffer = str.getBytes();
                    try {
                        response = di.uploadTriggersToDispatcher(buffer, cID, triggerSeqID);
                    } catch (RemoteException e) {
//                        e.printStackTrace();
                    }

                    if (response != null) {
                        System.out.println("Dispatcher Recieved the triggrs from the client and the response is " + response);
                    } else {
                        ClientUIControler.appendResults("Dispatcher service not working or connection to the Dispatcher service failed" + "\n");
                        return;
                    }
//}
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        });
    }

    public void initSendEventsStream() {
        events = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                while (true) {

                    String str = EventTriggerGenerator.getNextTrigger();
                    String cID = "1";
                    int eventsSeqID = 1;
                    String response = null;

                    byte[] buffer = str.getBytes();
                    try {
                        response = di.uploadEventsToDispatcher(buffer, cID, eventsSeqID);
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

    public void setDispatcherObj(Object obj) {
        di = (DispInterface) obj;
    }

    public Object getDispatcherObj() {
        return di;
    }
}
