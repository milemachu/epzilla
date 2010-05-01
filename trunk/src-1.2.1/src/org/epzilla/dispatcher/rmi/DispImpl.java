package org.epzilla.dispatcher.rmi;

import org.epzilla.dispatcher.dataManager.ClusterLeaderIpListManager;
import org.epzilla.dispatcher.dataManager.EventsCounter;
import org.epzilla.dispatcher.dataManager.TriggerManager;
import org.epzilla.dispatcher.logs.ReadLog;
import org.epzilla.client.rmi.ClientCallbackInterface;

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
            EventsCounter.setEventCount(eList.size());

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
                TriggerManager.addTriggerToList(tList.get(i));
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
    public String acceptNotifications() throws RemoteException {
        return null;
    }

    @Override
    public void registerCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        DispLoadBalance.updateIncLoad();
        if (!(clientList.contains(clientObject))) {
            clientList.addElement(clientObject);

            System.out.println("Registered new client " + clientObject);
            callBacks();
        }
    }

    @Override
    public void unregisterCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        DispLoadBalance.updateDecLoad();
        if (clientList.removeElement(clientObject)) {
            System.out.println("Unregistered the client ");
            callBacks();
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
    }

    @Override
    public void unRegisterClients(String ip, String id) {
        clientMap.remove(id);
    }

    @Override
    public String getClientIP(String clientID) throws RemoteException {
        if(clientMap.containsKey(clientID)){
          clientIpAdrs = (String) clientMap.get(clientID);
        }
        return clientIpAdrs;
    }

    @Override
    public void acceptLeaderIp(String ip, String clusterID) throws RemoteException {
        try {
            ClusterLeaderIpListManager.addIP(clusterID, ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void callBacks() throws RemoteException {
        for (int i = 0; i < clientList.size(); i++) {
            ClientCallbackInterface nextClient = clientList.elementAt(i);
            nextClient.notifyClient("No. of registered clients=" + clientList.size());
        }
    }


}
