package org.epzilla.leader.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.epzilla.leader.EpzillaProcess;
import org.epzilla.leader.Status;

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
		//Message from the elected leader
		//Update the leader variable to receive the pulse and ping 

	}

	/**
	 * This method can be called by any other node in the cluster to get the status{Leader,Non leader, Unknown}
	 * Will return the current status of the node to the requester.
	 */
	public String getStatus() throws RemoteException {
		
		return EpzillaProcess.getInstance().getStatus();
	}

	/**
	 * This method can be called by any member node of the node cluster and will return the Unique ID assigned 
	 * this node to the requester.
	 * Will be a non negative integer.
	 */
	public int getUID() throws RemoteException {
		
		return EpzillaProcess.UID;
	}

	/**
	 * This method will be called by any other member in the node cluster to check whether this node is the Leader.
	 * Instead of this, you may use the method getStatus() to get the status of this node. 
	 */
	public boolean isLeader() throws RemoteException {
		
		return EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(Status.LEADER.toString())? true:false;
	}

	/**
	 * This is the method which accepts control data and other type of messages. The messages received to this 
	 * method will be forwarded to the message decoder and will be assigned to a different thread to deal with it. 
	 */
	public void receiveMessage(String message) throws RemoteException {
		// Create the thread and pass a decode messenger to it.

	}

	/**
	 * This method will return the boolean whether this is the default leader of the respective STM based cluster.
	 * Any node can invoke this.
	 */
	public boolean isDefaultLeader() throws RemoteException {
		return EpzillaProcess.getInstance().isDefaultLeader();
		
	}
	

}
