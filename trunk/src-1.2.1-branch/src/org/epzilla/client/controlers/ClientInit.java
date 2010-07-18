package org.epzilla.client.controlers;

import org.epzilla.client.xml.ClientTimeSettings;
import org.epzilla.dispatcher.rmi.DispInterface;
import org.epzilla.dispatcher.rmi.TriggerRepresentation;
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
 * This class use to to initialize the client. Sending of Events and Triggers are managed
 * by this class.
 * Author: Chathura
 * Date: Mar 8, 2010
 * Time: 12:40:41 PM
 */
public class ClientInit extends Thread {
    private static String clientID;
    private static DispInterface di;
    private static Thread trigger;
    private static Thread events;
    private static String dispIP;
    private static HashMap<String, Object> dispMap = new HashMap<String, Object>();
    private static volatile boolean isTriggersLive = true;
    private static volatile boolean isEventsLive = true;
    private static int eventsSeqID = 1;
    private static int SENDING_INTERVAL_TIME;
    private static int INIT_INTERVAL_TIME;
    private static int INIT_INTERVAL_TRIGGER;
    private static int SENDING_INTERVAL_TRIGGER;
    private static int TRIGGER_SLEEP_TIME;
    private static int INIT_TRIGGER_LOOP;
    private static int PRIOR_TRIGGER_LOOP;

    public ClientInit() {
    }
    /*
   Create reference to the Dispatcher Service
    */

    /**
     * lookup dispatcher interface and get remote reference
     * @param ip
     * @param name
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    public static void lookUp(String ip, String name) throws MalformedURLException, NotBoundException, RemoteException {
        if (!dispMap.containsKey(ip)) {
            String url = "rmi://" + ip + "/" + name;
            DispInterface di = (DispInterface) Naming.lookup(url);
            setDispatcherObj(di);
            dispMap.put(ip, di);
        }
    }

    /**
     * connenct to the dispatcher service,   Intialize the send operation
     * @param ip
     * @param name
     * @param clientID
     * @throws MalformedURLException
     * @throws NotBoundException
     * @throws RemoteException
     */
    public static void initSend(String ip, String name, String clientID) throws MalformedURLException, NotBoundException, RemoteException {
        lookUp(ip, name);
        ClientInit.clientID = clientID;
        ClientInit.dispIP = ip;
        loadSettings();
    }

    /*
    initialize trigger simulation
     */

    public static void initTrigers() {
        isTriggersLive = true;
        initSendTriggerStream(dispIP);
        trigger.start();
        ClientUIControler.appendResults("Start Trigger sending process....");
    }

    /**
     * initialize the events simulation
     */

    public static void initEvents() {
        isEventsLive = true;
        initSendEventsStream(dispIP);
        events.start();
        ClientUIControler.appendResults("Start Event sending process....");
    }

    public static void initSendTriggerStream(final String ip) {
        trigger = new Thread(new Runnable() {
            String response = null;
            int triggerSeqID = 1;

            @Override
            public void run() {
                try {
                    Thread.sleep(INIT_INTERVAL_TRIGGER);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                di = (DispInterface) dispMap.get(ip);

                ArrayList<String> triggers = new ArrayList<String>();
                for (int i = 0; i < INIT_TRIGGER_LOOP; i++) {
                    triggers.add(EventTriggerGenerator.getNextTrigger());
                    try {
                        Thread.sleep(TRIGGER_SLEEP_TIME);
                    } catch (InterruptedException e) {
                        Logger.error("Interrupted: ", e);
                    }
                }
                try {
                    response = di.uploadTriggersToDispatcher(triggers, clientID, triggerSeqID);
                } catch (Exception e) {
                    ClientUIControler.appendResults("Connection to the Dispatcher service failed, trigger sending stoped, Perform Lookup operation..." + "\n");
                    isTriggersLive = false;
                }

                if (response != null) {
                    ClientUIControler.appendResults("Dispatcher Received the Trigger Stream" + "\n");
                }
                try {
                    Thread.sleep(SENDING_INTERVAL_TRIGGER);
                } catch (InterruptedException e) {
                    Logger.error("Interrupted :", e);
                }


                while (isTriggersLive) {
                    triggers = new ArrayList<String>();
                    for (int i = 0; i < PRIOR_TRIGGER_LOOP; i++) {
                        triggers.add(EventTriggerGenerator.getNextTrigger());
                    }
                    try {
                        response = di.uploadTriggersToDispatcher(triggers, clientID, triggerSeqID);
                    } catch (Exception e) {
                        ClientUIControler.appendResults("Connection to the Dispatcher service failed, trigger sending stoped, Perform Lookup operation..." + "\n");
                        isTriggersLive = false;
                    }

                    if (response != null) {
                        ClientUIControler.appendResults("Dispatcher Received the Trigger Stream" + "\n");
                    }
                    try {
                        Thread.sleep(SENDING_INTERVAL_TRIGGER);
                    } catch (InterruptedException e) {
                        Logger.error("Interrupted: ", e);
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
                    Thread.sleep(INIT_INTERVAL_TIME);
                } catch (InterruptedException e) {
                    Logger.error("", e);
                }

                di = (DispInterface) dispMap.get(ip);

                while (isEventsLive) {
                    String event = EventTriggerGenerator.getNextEvent();
                    try {
                        response = di.uploadEventsToDispatcher(event, clientID, eventsSeqID);
                        eventsSeqID++;
                        try {
                            Thread.sleep(SENDING_INTERVAL_TIME);
                        } catch (InterruptedException e) {
                            Logger.error("", e);
                        }
                    } catch (RemoteException e) {
                        isEventsLive = false;
                        ClientUIControler.appendResults("Connection to the Dispatcher service failed, events sending stoped, Perform Lookup operation.." + "\n");
                    }

                    if (response != null) {
                        Logger.log("Dispatcher Recieved the events from the client and the response is " + response);
                    }
                }
            }
        });
    }

    /**
     * accept use input triggers
     * @param trigger
     */
    public static void sendCustomTriggers(String trigger) {
        String response = null;

        try {

            di = (DispInterface) dispMap.get(dispIP);
            ArrayList<String> triggers = new ArrayList<String>();
            triggers.add(trigger);
            response = di.uploadTriggersToDispatcher(triggers, clientID, 1);
        } catch (RemoteException e) {
            ClientUIControler.appendResults("Connection to the Dispatcher service failed, trigger sending stoped, Perform Lookup operation..." + "\n");
        } catch (NullPointerException ex) {
            ClientUIControler.appendResults("Connection to the Dispatcher service failed, trigger sending stoped, Perform Lookup operation..." + "\n");
        }

        if (response != null) {
            ClientUIControler.appendResults("Dispatcher Received the Trigger Stream" + "\n");
        }
        if(response==null){
             ClientUIControler.appendResults("Dispatcher Doesn't Received the Trigger Stream" + "\n"); 
        }
    }


    /**
     * load setting details from the XML file
     */
    private static void loadSettings() {
        ArrayList<String[]> settingsList = ClientTimeSettings.getClientTimeIntervals("client_timeIntervals.xml");
        String[] settings = settingsList.get(0);
        if (settings != null) {
            INIT_INTERVAL_TIME = Integer.valueOf(settings[0]);
            SENDING_INTERVAL_TIME = Integer.valueOf(settings[1]);
            INIT_INTERVAL_TRIGGER = Integer.valueOf(settings[2]);
            SENDING_INTERVAL_TRIGGER = Integer.valueOf(settings[3]);
            TRIGGER_SLEEP_TIME = Integer.valueOf(settings[4]);
            INIT_TRIGGER_LOOP = Integer.valueOf(settings[5]);
            PRIOR_TRIGGER_LOOP = Integer.valueOf(settings[6]);
        }
    }
    /*
   Stop the trigger sending
    */

    public static void stopTriggerStream() {
        isTriggersLive = false;
    }
    /*
   Stop the Event sending
    */

    public static void stopEventStream() {
        isEventsLive = false;
    }

    private static void setDispatcherObj(Object obj) {
        di = (DispInterface) obj;
    }

    public static Object getDispatcherObject() {
        return di;
    }
    /*
    delete triggers as requsted by client
     */

    public static void deleteTriggers(String clientID, ArrayList<TriggerRepresentation> list) {

        try {
            DispInterface di = (DispInterface) ClientInit.getDispatcherObject();
            di.deleteTriggers(list, clientID);

        } catch (Exception ex) {
            Logger.error("Trigger deletion error:", ex);
        }


    }
    /*
    get all triggers as requested by client
     */

    public static ArrayList<TriggerRepresentation> getAllTriggersFromDispatcher(String clientID) {
        try {
            DispInterface di = (DispInterface) ClientInit.getDispatcherObject();
            return di.getAllTriggers(clientID);
        } catch (Exception ex) {
            Logger.error("Trigger receive error:", ex);
        }
        return null;
    }
}
