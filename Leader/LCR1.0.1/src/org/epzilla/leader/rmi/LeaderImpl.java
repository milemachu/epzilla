package org.epzilla.leader.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class LeaderImpl extends UnicastRemoteObject implements LeaderInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 708757744140286512L;

	protected LeaderImpl() throws RemoteException {
		super();
		
	}

	/**
	 * This method will be used to inform the non leaders about the elected leader
	 * Will only be invoked by a leader
	 */
	public void electedLeader(String message) throws RemoteException {
		// TODO Auto-generated method stub

	}

	/**
	 * This method can be called by any other node in the cluster to get the status{Leader,Non leader, Unknown}
	 * Will return the current status of the node to the requester.
	 */
	public String getStatus() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method can be called by any member node of the node cluster and will return the Unique ID assigned 
	 * this node to the requester.
	 * Will be a non negative integer.
	 */
	public int getUID() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * This method will be called by any other member in the node cluster to check whether this node is the Leader.
	 * Instead of this, you may use the method getStatus() to get the status of this node. 
	 */
	public boolean isLeader() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This is the method which accepts control data and other type of messages. The messages received to this 
	 * method will be forwarded to the message decoder and will be assigned to a different thread to deal with it. 
	 */
	public void receiveMessage(String message) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
