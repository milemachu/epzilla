package org.epzilla.dispatcher.rmi;

import net.epzilla.stratification.immediate.ApproximateDispatcher;
import org.epzilla.client.rmi.ClientCallbackInterface;
import org.epzilla.dispatcher.dataManager.*;
import org.epzilla.dispatcher.logs.ReadLog;
import org.epzilla.dispatcher.notificationSystem.ClientNotifier;
import org.epzilla.util.Logger;

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
    private ArrayList<String> recoverTriggers = new ArrayList<String>();
    private String clientIP;


    protected DispImpl() throws RemoteException {

    }

    @Override
    public String uploadEventsToDispatcher(byte[] event, String clientID, int eventSeqID) throws RemoteException {
        try {
            EventsCounter.setInEventCount(1);
            EventManager.sendEvents(event, clientID);
            return "OK";
        } catch (Exception e) {
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
               Logger.log(x);
           }

            TriggerManager.addAllTriggersToList(tList, clientID);
//            }
            toReturn = "OK";
            ClientNotifier.getNotifications(getClientIP(clientID),"Dispatcher Received the Trigger Stream");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public String deleteTriggers(ArrayList<String> list, String cID, int triggerSeqID) throws RemoteException {

        return null;
    }

    @Override
    public void getNotifications(String notification, String clientID) throws RemoteException {
        clientIP = getClientIP(clientID);

    }

    @Override
    public void registerCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        DispLoadBalance.updateIncLoad();
        if (!(clientList.contains(clientObject))) {
            clientList.addElement(clientObject);
            Logger.log("Registered new client " + clientObject);
        }
    }

    @Override
    public void unregisterCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        DispLoadBalance.updateDecLoad();
        if (clientList.removeElement(clientObject)) {
            Logger.log("Unregistered the client ");
        } else {
            Logger.log(
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
    public void getLeaderIp(String ip) throws RemoteException {
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