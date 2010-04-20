package org.epzilla.dispatcher.rmi;

import org.epzilla.dispatcher.dataManager.ClusterLeaderIpListManager;
import org.epzilla.dispatcher.dataManager.EventsCounter;
import org.epzilla.dispatcher.dataManager.TriggerManager;
import org.epzilla.ui.rmi.ClientCallbackInterface;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;
import org.epzilla.dispatcher.logs.*;

public class DispImpl extends UnicastRemoteObject implements DispInterface {

    private Vector<ClientCallbackInterface> clientList = new Vector<ClientCallbackInterface>();

    protected DispImpl() throws RemoteException {
        //super();
    }

    public String uploadEventsToDispatcher(byte[] stream, String clientID, int eventSeqID) throws RemoteException {
        try {
            EventsCounter.setEventCount();
            return "OK";
        } catch (Exception e) {
            System.err.println("FileServer exception: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String uploadTriggersToDispatcher(byte[] stream, String clientID, int triggerSeqID) throws RemoteException {
        String toReturn = null;
        try {
            TriggerManager.addTriggerToList(stream);
            toReturn = "OK";
            return toReturn;

        } catch (Exception e) {
            System.err.println("FileServer exception: " + e.getMessage());
        }
        return toReturn;
    }

    @Override
    public String acceptNotifications() throws RemoteException {
        return null;
    }

    @Override
    public void registerCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException {
        DispLoadBalance.updateLoad();
        if (!(clientList.contains(clientObject))) {
            clientList.addElement(clientObject);
            System.out.println("Registered new client " + clientObject);
            calllbacks();
        }
    }

    @Override
    public void unregisterCallback(ClientCallbackInterface clientObject) throws RemoteException {
        if (clientList.removeElement(clientObject)) {
            System.out.println("Unregistered client ");
        } else {
            System.out.println(
                    "unregister: clientwasn't registered." + clientObject);
        }
    }
    @Override
    public void replayLogs(String clusterID,String leaderIP) throws RemoteException {
        ReadLog.readLog(clusterID);
    }
    public synchronized void calllbacks() throws RemoteException {
        for (int i = 0; i < clientList.size(); i++) {
            ClientCallbackInterface nextClient = clientList.elementAt(i);
            nextClient.notifyClient("Events hit=" + clientList.size());
        }
    }
    @Override
    public void acceptLeaderIp(String ip,String clusterID) throws RemoteException {
        try {
            ClusterLeaderIpListManager.addIP(clusterID, ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
