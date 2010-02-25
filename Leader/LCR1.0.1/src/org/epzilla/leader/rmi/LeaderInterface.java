package org.epzilla.leader.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface LeaderInterface extends Remote {

	/**
	 * 
	 * @return the UID assigned to this machine  by Administrator
	 * @throws RemoteException
	 */
	public int getUID() throws RemoteException;
	
	/** 
	 * When the leader election is completed and a leader is elected this method will be called 
	 * by the member in the right hand side (above the list)
	 * @param Leader Election Message
	 * @throws RemoteException
	 */
	public void electedLeader(String message) throws RemoteException;
	
	/**
	 * If some other node need to find out whether this node is the leader, this is called.
	 * @return boolean whether the leader or not
	 * @throws RemoteException
	 */
	public boolean isLeader() throws RemoteException;
	
	/**
	 * This is to be called to get the status of the node
	 * @return Status of the node
	 * @throws RemoteException
	 */
	public String getStatus() throws RemoteException;
	
	/**
	 * This is general method to pass the messages from host to host.
	 * This message will be decoded and will be passed to the Busi. Logic.
	 * @param message
	 * @throws RemoteException
	 */
	public void receiveMessage(String message) throws RemoteException;
	
}
