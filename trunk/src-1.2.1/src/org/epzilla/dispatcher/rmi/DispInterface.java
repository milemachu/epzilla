package org.epzilla.dispatcher.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import org.epzilla.ui.rmi.*;

public interface DispInterface extends Remote {

	public String  uploadEventsToDispatcher(byte[] stream,String cID,int eventSeqID) throws RemoteException;
	public String uploadTriggersToDispatcher(byte[] stream,String cID,int triggerSeqID) throws RemoteException;
	public String acceptNotifications()throws RemoteException;
	public void registerCallback(ClientCallbackInterface clientObject) throws RemoteException, MalformedURLException, UnknownHostException, NotBoundException;
	public void unregisterCallback(ClientCallbackInterface clientObject)throws RemoteException;
	public void acceptLeaderIp(String ip,String clusterID) throws RemoteException;
    public void replayLogs(String clusterID,String leadeIP)throws RemoteException;
   
}