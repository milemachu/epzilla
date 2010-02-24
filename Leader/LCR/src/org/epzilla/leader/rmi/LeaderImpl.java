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

	@Override
	public void electedLeader(String message) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getStatus() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getUID() throws RemoteException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isLeader() throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void receiveMessage(String message) throws RemoteException {
		// TODO Auto-generated method stub

	}

}
