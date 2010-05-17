package org.epzilla.dispatcher.rmi;

import net.epzilla.stratification.immediate.ApproximateDispatcher;
import org.epzilla.client.rmi.ClientCallbackInterface;
import org.epzilla.dispatcher.dataManager.*;
import org.epzilla.dispatcher.logs.ReadLog;
import org.epzilla.dispatcher.notificationSystem.ClientNotifier;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class DispImpl extends UnicastRemoteObject implements DispInterface {

    private Vector<ClientCallbackInterface> clientList = new Vector<ClientCallbackInterface>();
    private HashMap clientMap = new HashMap<String, String>();
    private ArrayList<String> arr = new ArrayList<String>();
    private ArrayList<String> recoverTriggers = new ArrayList<String>();
    private String clientIP;


    protected DispImpl() throws RemoteException {
        arr.add("Dispatcher Received the Trigger Stream");
    }

    @Override
    public String uploadEventsToDispatcher(String event, String clientID, int eventSeqID) throws RemoteException {
        try {
            EventsCounter.setInEventCount(1);
            EventManager.sendEventsToClusters(event, clientID);
            return "OK";
        } catch (Exception e) {
            System.err.println("FileServer exception");
            e.printStackTrace();
        }
        return null;
    }
             
    @Override
    public String uploadTriggersToDispatcher(ArrayList<String> tList, String clientID, int triggerSeqID) throws RemoteException {
        String toReturn = null;
        try  {
//            for (String aTList : tList) {
//             TriggerManager.addTriggerToList(aTList,clientID);
//             todo remove if problematic.
            // add to stm all at once.
            ApproximateDispatcher ad = new ApproximateDispatcher();

           for (String x: tList) {
               System.out.println(x);
           }

            TriggerManager.addAllTriggersToList(tList, clientID);
//            }
            toReturn = "OK";
            ClientNotifier.acceptNotifications(getClientIP(clientID), arr);

        } catch (Exception e) {
            System.err.println("FileServer exception: " + e.getMessage());
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public String deleteTriggers(ArrayList<String> list, String cID, int triggerSeqID) throws RemoteException {

        return null;
    }

    @Override
    public void acceptNotifications(ArrayList<String> notification, String clientID) throws RemoteException {
        clientIP = getClientIP(clientID);

    }

    @Override
    public void registerCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        DispLoadBalance.updateIncLoad();
        if (!(clientList.contains(clientObject))) {
            clientList.addElement(clientObject);

            System.out.println("Registered new client " + clientObject);
        }
    }

    @Override
    public void unregisterCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        DispLoadBalance.updateDecLoad();
        if (clientList.removeElement(clientObject)) {
            System.out.println("Unregistered the client ");
        } else {
            System.out.println(
                    "unregister: clientwasn't registered." + clientObject);
        }
    }

    @Override
    public void replayLogs(String clusterID, String leaderIP) throws RemoteException {
        recoverTriggers = (ArrayList<String>) ReadLog.readLog(clusterID);
    }

    @Override
    public void registerClients(String ip, String id) throws RemoteException {
        clientMap.put(id, ip);
        ClientManager.addClient(id, ip);
    }

    @Override
    public void unRegisterClients(String ip, String id) throws RemoteException {
        clientMap.remove(id);
        ClientManager.removeClient(id);
    }

    @Override
    public String getClientIP(String clientID) throws RemoteException {
        return ClientManager.getClientIp(clientID);
    }

    @Override
    public void acceptLeaderIp(String ip) throws RemoteException {
        try {
            String clusterID = ClusterIDGenerator.getClusterID(ip);
            ClusterLeaderIpListManager.addIP(clusterID, ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void callBacks(String clientID) throws RemoteException {
//        for (int i = 0; i < clientList.size(); i++) {
//            ClientCallbackInterface nextClient = clientList.elementAt(i);
//            nextClient.notifyClient("No. of registered clients=" + clientList.size());
//        }
    }


}
