package org.epzilla.dispatcher.rmi;

import org.epzilla.client.rmi.ClientCallbackInterface;
import org.epzilla.dispatcher.dataManager.ClientManager;
import org.epzilla.dispatcher.dataManager.ClusterLeaderIpListManager;
import org.epzilla.dispatcher.dataManager.EventsCounter;
import org.epzilla.dispatcher.dataManager.TriggerManager;
import org.epzilla.dispatcher.logs.ReadLog;

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
    private String clientIpAdrs = "";

    protected DispImpl() throws RemoteException {
    }

    @Override
    public String uploadEventsToDispatcher(ArrayList<String> eList, String clientID, int eventSeqID) throws RemoteException {
        try {
            EventsCounter.setInEventCount(eList.size());
//            EventManager.sendEventsToClusters(eList,clientID);
            return "OK";
        } catch (Exception e) {
            System.err.println("FileServer exception");
        }
        return null;
    }

    @Override
    public String uploadTriggersToDispatcher(ArrayList<String> tList, String clientID, int triggerSeqID) throws RemoteException {
        String toReturn = null;
        try {
            for (int i = 0; i < tList.size(); i++) {
                TriggerManager.addTriggerToList(tList.get(i), clientID);
            }
            toReturn = "OK";

        } catch (Exception e) {
            System.err.println("FileServer exception: " + e.getMessage());
        }
        return toReturn;
    }

    @Override
    public String deleteTriggers(ArrayList<String> list, String cID, int triggerSeqID) throws RemoteException {

        return null;
    }

    @Override
    public void acceptNotifications(ArrayList<String> notification, String clientID) throws RemoteException {
          String clientIP = getClientIP(clientID);
        
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
        ReadLog.readLog(clusterID);
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
//        if(clientMap.containsKey(clientID)){
//          clientIpAdrs = (String) clientMap.get(clientID);
//        }
        return ClientManager.getClientIp(clientID);
    }

    @Override
    public void acceptLeaderIp(String ip, String clusterID) throws RemoteException {
        try {
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
