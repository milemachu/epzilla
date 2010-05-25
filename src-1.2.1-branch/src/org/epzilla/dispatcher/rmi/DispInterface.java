package org.epzilla.dispatcher.rmi;

import org.epzilla.client.rmi.ClientCallbackInterface;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface DispInterface extends Remote {

    public String uploadEventsToDispatcher(byte[] event, String cID, int eventSeqID) throws RemoteException;

    public String uploadTriggersToDispatcher(ArrayList<String> tList, String cID, int triggerSeqID) throws RemoteException;

    public String deleteTriggers(ArrayList<String> list, String cID, int triggerSeqID) throws RemoteException;

    public void getNotifications(String notification,String clientID) throws RemoteException;

    public void registerCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException;

    public void unregisterCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException;

    public void getLeaderIp(int id,String ip) throws RemoteException;

    public void replayLogs(String clusterID, String leadeIP) throws RemoteException;

    public void registerClients(String ip, String id) throws RemoteException;

    public void unRegisterClients(String ip, String id) throws RemoteException;

    public String getClientIP(String clientID) throws RemoteException;

    public void performancceInfo(int cpuUsg, int mmUsg) throws RemoteException;

}