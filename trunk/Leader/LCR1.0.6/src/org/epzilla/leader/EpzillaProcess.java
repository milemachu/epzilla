package org.epzilla.leader;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;

import org.epzilla.leader.event.IEpzillaEvent;
import org.epzilla.leader.event.IEpzillaEventListner;
import org.epzilla.leader.event.ProcessStatusChangedEvent;
import org.epzilla.leader.event.PulseTimeoutEvent;
import org.epzilla.leader.rmi.LeaderMessageClient;

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
			// TODO change these according to the node
			setStatus(Status.UNKNOWN.toString());
			setDefaultLeader(true);
			setClusterLeader(InetAddress.getLocalHost());
			// Add these from some where
			setClusterIpList(new Vector<InetAddress>());
			// getClusterIpList().add(e)
			// Get these from the STM and update later. initially none
			setClusterAliveIpList(new Vector<InetAddress>());

		} catch (UnknownHostException e) {
			
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

	public synchronized void setClusterLeader(InetAddress clusterLeader) {
		this.clusterLeader = clusterLeader;
	}

	public synchronized InetAddress getClusterLeader() {
		return clusterLeader;
	}

	public synchronized void setClusterIpList(Vector<InetAddress> clusterIpList) {
		this.clusterIpList = clusterIpList;
	}

	public synchronized Vector<InetAddress> getClusterIpList() {
		return clusterIpList;
	}

	public synchronized void setClusterAliveIpList(
			Vector<InetAddress> clusterAliveIpList) {
		this.clusterAliveIpList = clusterAliveIpList;
	}

	public synchronized Vector<InetAddress> getClusterAliveIpList() {
		return clusterAliveIpList;
	}

	public synchronized void addEpzillaEventListener(IEpzillaEventListner listener) {
		if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
				Status.LEADER.toString()))
			EpzillaLeader.getInstance().getClusterClientList().add(listener);
	}
	
	public synchronized void removeEpzillaEventListener(IEpzillaEventListner listener) {
		if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
				Status.LEADER.toString()) && EpzillaLeader.getInstance().getClusterClientList()!=null){
			EpzillaLeader.getInstance().getClusterClientList().removeElement(listener);
		}
	}
	
	public synchronized void fireEpzillaEvent(IEpzillaEvent event){
		if(event instanceof ProcessStatusChangedEvent){
			//Check whether the new status is nonLeader or Unknown
			if(!EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(Status.LEADER.toString())){
			EpzillaLeader.getInstance().setClusterClientList(null);
			}
			System.out.println("Epzilla Process Status changed Event Fired.");
		}else if(event instanceof PulseTimeoutEvent){
			// Send a pulse to each and every listener
			if(EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(Status.LEADER.toString())){
				Iterator<IEpzillaEventListner> iterator=EpzillaLeader.getInstance().getClusterClientList().listIterator();
				while (iterator.hasNext()) {
					IEpzillaEventListner listner = (IEpzillaEventListner) iterator
							.next();
					
					//PULSE message sent from here
					try {
						LeaderMessageClient.sendPulse(listner.getData());
					} catch (MalformedURLException e) {
						
						e.printStackTrace();
					} catch (RemoteException e) {
						
						e.printStackTrace();
					} catch (UnknownHostException e) {
						
						e.printStackTrace();
					} catch (NotBoundException e) {
					
						e.printStackTrace();
					}
				}
				
				//TODO Implement and RESET the Timer for Pulse
				
				System.out.println("Pulse sending time out event fired.");
			}
		}
	}

}
