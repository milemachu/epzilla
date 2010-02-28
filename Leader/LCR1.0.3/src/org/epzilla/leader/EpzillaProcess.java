package org.epzilla.leader;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

public class EpzillaProcess {

	// This is the UID of this host
	public static byte UID = 1;

	private String status;
	private boolean isDefaultLeader;
	private InetAddress clusterLeader;
	private Vector<InetAddress> clusterIpList;
	private Vector<InetAddress> clusterAliveIpList;

	// Private constructor prevents instantiation from other classes
	private EpzillaProcess() {
		try {
			//TODO change these according to the node
			setStatus(Status.UNKNOWN.toString());
			setDefaultLeader(true);
			setClusterLeader(InetAddress.getLocalHost());
			//Add these from some where 
			setClusterIpList(new Vector<InetAddress>());
//			getClusterIpList().add(e)
			//Get these from the STM and update later. intiall none
			setClusterAliveIpList(new Vector<InetAddress>());
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class EpzillaProcessHolder {
		private static final EpzillaProcess INSTANCE = new EpzillaProcess();
	}

	public static EpzillaProcess getInstance() {
		return EpzillaProcessHolder.INSTANCE;
	}

	public synchronized String getStatus() {
		return status;
	}

	public synchronized void setStatus(String status) {

		this.status = status;
	}

	public synchronized void setDefaultLeader(boolean isDefaultLeader) {
		this.isDefaultLeader = isDefaultLeader;
	}

	public synchronized boolean isDefaultLeader() {
		return isDefaultLeader;
	}

	public void setClusterLeader(InetAddress clusterLeader) {
		this.clusterLeader = clusterLeader;
	}

	public InetAddress getClusterLeader() {
		return clusterLeader;
	}

	public void setClusterIpList(Vector<InetAddress> clusterIpList) {
		this.clusterIpList = clusterIpList;
	}

	public Vector<InetAddress> getClusterIpList() {
		return clusterIpList;
	}

	public void setClusterAliveIpList(Vector<InetAddress> clusterAliveIpList) {
		this.clusterAliveIpList = clusterAliveIpList;
	}

	public Vector<InetAddress> getClusterAliveIpList() {
		return clusterAliveIpList;
	}

}
