package org.epzilla.dispatcher.rmi;

import org.epzilla.ui.rmi.ClientCallbackInterface;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface DispInterface extends Remote {

    public String uploadEventsToDispatcher(ArrayList<String> eList, String cID, int eventSeqID) throws RemoteException;

    public String uploadTriggersToDispatcher(ArrayList<String> tList, String cID, int triggerSeqID) throws RemoteException;

    public String deleteTriggers(ArrayList<String> list, String cID, int triggerSeqID) throws RemoteException;

    public String acceptNotifications() throws RemoteException;

    public void registerCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException;

    public void unregisterCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException;

    public void acceptLeaderIp(String ip, String clusterID) throws RemoteException;

    public void replayLogs(String clusterID, String leadeIP) throws RemoteException;

    public void registerClients(String ip, String id) throws RemoteException;

    public void unRegisterClients(String ip, String id) throws RemoteException;

    public String getClientIP(String clientID) throws RemoteException;

}