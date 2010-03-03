package org.epzilla.dispatcher;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DispInterface extends Remote {

	public String  uploadEventsToDispatcher(byte[] stream,String cID,int eventSeqID) throws RemoteException;
	public String uploadTriggersToDispatcher(byte[] stream,String cID,int triggerSeqID) throws RemoteException;
	public byte[] downloadFileFromServer(String fileName) throws RemoteException, IOException;
	public void acceptNotifications()throws RemoteException;
}