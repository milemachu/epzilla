package org.epzilla.dispatcher;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DispInterface extends Remote {
	
	/**
	 * 
	 * @param Input byte stream from the Client. This contains the file.
	 * @return Result whether the file has received and stored correctly. 
	 * @throws RemoteException
	 */
	public String  uploadFileToDispatcher(byte[] stream) throws RemoteException;
	
	/**
	 * 
	 * @param FileName to download. 
	 * @return the byte stream of file information.
	 * @throws RemoteException
	 * @throws IOException 
	 */
	public byte[] downloadFileFromServer(String fileName) throws RemoteException, IOException;
	
}