package org.epzilla.daemon.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the daemon service implemented for cluster node load balancing. 
 * @author Administrator
 *
 */
public interface DaemonInterface extends Remote{
	
	/**
	 * Used to kill the epzilla process.
	 * @throws RemoteException
	 */
	public boolean sleepEpzilla() throws RemoteException;
	
	/**
	 * Used to start the epZilla process
	 * @throws RemoteException
	 */
	public boolean wakeEpZilla() throws RemoteException;

}
