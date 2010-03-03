package org.epzilla.dispatcher;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DispInterface extends Remote {

	public String  uploadEventsToDispatcher(byte[] stream,int cID,int eventSeqID) throws RemoteException;
	public String uploadTriggersToDispatcher(byte[] stream,int cID,int triggerSeqID) throws RemoteException;
	public byte[] downloadFileFromServer(String fileName) throws RemoteException, IOException;
	
}