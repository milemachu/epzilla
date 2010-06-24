package org.epzilla.leader.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.epzilla.leader.Epzilla;
import org.epzilla.leader.EpzillaLeaderPubSub;
import org.epzilla.leader.event.listner.IEpzillaEventListner;
import org.epzilla.leader.message.RejectReason;
import org.epzilla.leader.message.RmiMessageClient;
import org.epzilla.leader.message.RmiMessageHandler;

/**
 * This is the LeaderService implementation class. 
 * @author Administrator
 *
 */
public class LeaderServiceImpl extends UnicastRemoteObject implements LeaderInterface {
	
	private RmiMessageHandler messageHandler;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5270364201519480615L;

	public LeaderServiceImpl() throws RemoteException {
		super();
		messageHandler=new RmiMessageHandler();		
	}

	@Override
	public void addListener(final IEpzillaEventListner listener)
			throws RemoteException {
		final boolean result=EpzillaLeaderPubSub.addClientListner(listener);		
		Thread pulsator=new Thread(new Runnable() {
			public void run() {
				if(result)
					RmiMessageClient.sendPulse(listener.getData());
				else
					RmiMessageClient.sendRequestNotAccepted(listener.getData(), RejectReason.NOT_ALLOWED_TO_REGISTER_LISTNER_NOT_LEADER);				
			}
		});
		pulsator.start();
	}

	@Override
	public String getClusterLeader() throws RemoteException {
		return Epzilla.getClusterLeader();
	}

	@Override
	public String getStatus() throws RemoteException {
		return Epzilla.getStatus();
	}

	@Override
	public long getUID() throws RemoteException {
		return Epzilla.getUID();
	}

	@Override
	public boolean isDefaultLeader() throws RemoteException {
		return Epzilla.isDefaultLeader();
	}

	@Override
	public boolean isLeader() throws RemoteException {
		return Epzilla.isLeader();
	}

	@Override
	public boolean isLeaderElectionRunning() throws RemoteException {
		return Epzilla.isLeaderElectionRunning();
	}

	@Override
	public void receiveMessage(String message) throws RemoteException {
		messageHandler.enqueueMessage(message);
	}

	@Override
	public void removeListener(IEpzillaEventListner listener)
			throws RemoteException {
		EpzillaLeaderPubSub.removeClientListner(listener);
	}

}
