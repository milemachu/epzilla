package org.epzilla.dispatcher;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.epzilla.ui.ClientCallbackInterface;

public interface DispInterface extends Remote {

	public String  uploadEventsToDispatcher(byte[] stream,String cID,int eventSeqID) throws RemoteException;
	public String uploadTriggersToDispatcher(byte[] stream,String cID,int triggerSeqID) throws RemoteException;
	public String acceptNotifications()throws RemoteException;
	public void registerCallback(ClientCallbackInterface clientObject) throws RemoteException;
	public void unregisterCallback(ClientCallbackInterface clientObject)throws RemoteException;

}