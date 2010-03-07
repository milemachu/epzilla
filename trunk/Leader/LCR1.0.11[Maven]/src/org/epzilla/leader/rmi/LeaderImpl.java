package org.epzilla.leader.rmi;

import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.epzilla.leader.EpzillaProcess;
import org.epzilla.leader.Status;
import org.epzilla.leader.event.IEpzillaEventListner;
import org.epzilla.leader.event.ListenerRegisteredEvent;
import org.epzilla.leader.message.MessageDecoder;

public class LeaderImpl extends UnicastRemoteObject implements LeaderInterface {

	/**
	 * 
	 */
	private static final long serialVersionUID = 708757744140286512L;

	protected LeaderImpl() throws RemoteException {
		super();

	}

	/**
	 * This method will be used to inform the non leaders about the elected
	 * leader Will only be invoked by a leader
	 * 
	 * @throws UnknownHostException
	 * @throws UnknownHostException
	 */
	public void electedLeader(String message) throws RemoteException,
			UnknownHostException {
		// Message from the elected leader
		// Update the leader variable to receive the pulse and ping
		MessageDecoder.getInstance().decodeMessage(message);
	}

	/**
	 * This method can be called by any other node in the cluster to get the
	 * status{Leader,Non leader, Unknown} Will return the current status of the
	 * node to the requester.
	 */
	public String getStatus() throws RemoteException {

		return EpzillaProcess.getInstance().getStatus();
	}

	/**
	 * This method can be called by any member node of the node cluster and will
	 * return the Unique ID assigned this node to the requester. Will be a non
	 * negative integer.
	 */
	public int getUID() throws RemoteException {

		return EpzillaProcess.UID;
	}

	/**
	 * This method will be called by any other member in the node cluster to
	 * check whether this node is the Leader. Instead of this, you may use the
	 * method getStatus() to get the status of this node.
	 */
	public boolean isLeader() throws RemoteException {

		return EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
				Status.LEADER.toString()) ? true : false;
	}

	/**
	 * This is the method which accepts control data and other type of messages.
	 * The messages received to this method will be forwarded to the message
	 * decoder and will be assigned to a different thread to deal with it.
	 * 
	 * @throws UnknownHostException
	 */
	public void receiveMessage(String message) throws RemoteException,
			UnknownHostException {
		// get the Message decoder instance and decode it..

		MessageDecoder.getInstance().decodeMessage(message);

	}

	/**
	 * This method will return the boolean whether this is the default leader of
	 * the respective STM based cluster. Any node can invoke this.
	 */
	public boolean isDefaultLeader() throws RemoteException {
		return EpzillaProcess.getInstance().isDefaultLeader();

	}

	/**
	 * This method will be called when the client start to register them to
	 * receive message/event from the leader
	 */
	public void addListener(IEpzillaEventListner listener)
			throws RemoteException {
		EpzillaProcess.getInstance().addEpzillaEventListener(listener);
		EpzillaProcess.getInstance().fireEpzillaEvent(new ListenerRegisteredEvent(listener.getData()));

	}

	/**
	 * This method is used to unregister a registered user from receiving events
	 * from the leader.
	 */
	public void removeListener(IEpzillaEventListner listener)
			throws RemoteException {
		EpzillaProcess.getInstance().removeEpzillaEventListener(listener);
	}

	/**
	 * This is used to get the Leader address from a remote machine without
	 * running a Leader Election.
	 */
	public String getClusterLeader() throws RemoteException {
		return EpzillaProcess.getInstance().getClusterLeader().getHostAddress();
	}

}
