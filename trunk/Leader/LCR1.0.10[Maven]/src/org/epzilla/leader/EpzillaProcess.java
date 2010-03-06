package org.epzilla.leader;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Timer;
import java.util.Vector;

import org.epzilla.leader.event.EpZillaListener;
import org.epzilla.leader.event.EpzillaErrorStateExitTimer;
import org.epzilla.leader.event.EpzillaPulseReceiveTimer;
import org.epzilla.leader.event.EpzillaPulseSendTimer;
import org.epzilla.leader.event.ErrorOccurredEvent;
import org.epzilla.leader.event.IEpzillaEvent;
import org.epzilla.leader.event.IEpzillaEventListner;
import org.epzilla.leader.event.ListenerRegisteredEvent;
import org.epzilla.leader.event.PingReceivedEvent;
import org.epzilla.leader.event.ProcessStatusChangedEvent;
import org.epzilla.leader.event.PulseIntervalTimeoutEvent;
import org.epzilla.leader.event.PulseNotReceivedTimeoutEvent;
import org.epzilla.leader.event.PulseReceivedEvent;
import org.epzilla.leader.rmi.LeaderMessageClient;

public class EpzillaProcess {

	// This is the UID of this host
	public static byte UID = 1;

	private String status;
	private boolean isDefaultLeader;
	private InetAddress clusterLeader;
	private Vector<InetAddress> clusterIpList;
	private Vector<InetAddress> clusterAliveIpList;
	private final Timer timer=new Timer();

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

	public synchronized void addEpzillaEventListener(
			IEpzillaEventListner listener) {
		if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
				Status.LEADER.toString()))
			EpzillaLeader.getInstance().getClusterClientList().add(listener);
	}

	public synchronized void removeEpzillaEventListener(
			IEpzillaEventListner listener) {
		if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
				Status.LEADER.toString())
				&& EpzillaLeader.getInstance().getClusterClientList() != null) {
			EpzillaLeader.getInstance().getClusterClientList().removeElement(
					listener);
		}
	}

	public synchronized void fireEpzillaEvent(IEpzillaEvent event) {		

		if (event instanceof ProcessStatusChangedEvent) {
			// Check whether the new status is nonLeader or Unknown
			// If above true, reset timer and epZilla leader
			if (!EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
					Status.LEADER.toString())) {
				EpzillaLeader.getInstance().setClusterClientList(null);
			}
			timer.cancel();
			System.out.println("Epzilla Process Status changed Event Fired. All the scheduled tasks are removed.");
		} else if (event instanceof PulseIntervalTimeoutEvent) {
			// Send a pulse to each and every listener
			if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
					Status.LEADER.toString())) {
				Iterator<IEpzillaEventListner> iterator = EpzillaLeader
						.getInstance().getClusterClientList().listIterator();
				while (iterator.hasNext()) {
					IEpzillaEventListner listner = (IEpzillaEventListner) iterator
							.next();

					// PULSE message sent from here
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

				// TODO Implement and RESET the Timer for Pulse
				timer.schedule(new EpzillaPulseSendTimer(), 240000);
				System.out.println("Pulse sending time out event fired.");
			}
		} else if (event instanceof PulseNotReceivedTimeoutEvent) {
			// Client has not received the pulse after 360 default interval.
		
			try {
				if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
						Status.NON_LEADER.toString())) {
					
					//Has to be the ip below/above this host
					String leader=LeaderMessageClient.getClusterLeaderFromRemote("IP Next to me");
					if(EpzillaProcess.getInstance().getClusterLeader().getHostAddress().equalsIgnoreCase(leader)){
						//Same leader. Just send a PING
						LeaderMessageClient.sendPing(EpzillaProcess.getInstance()
								.getClusterLeader().getHostAddress());
					}else{
						//Different Leader. Still need to send a pulse.
						//When pulse received, normal timers will be back.
						EpzillaProcess.getInstance().setClusterLeader(InetAddress.getByName(leader));
						LeaderMessageClient.registerListenerWithLeader(leader, new EpZillaListener());
					}
					
					//In both cases, they expect a pulse. So have to re-init the timer for that.
				
//					timer.cancel();
//					timer.schedule(new EpzillaPulseReceiveTimer(), 360000);
					
				}
				// Wait 10 mins and exit from the system
				//They have waited at lease once. They are trying to start it over.
				//If this time response received, every thing will be back to normal.
				timer.cancel();
				timer.schedule(new EpzillaErrorStateExitTimer(), 600000);
				
			} catch (MalformedURLException e) {

				e.printStackTrace();
			} catch (RemoteException e) {

				e.printStackTrace();
			} catch (UnknownHostException e) {

				e.printStackTrace();
			} catch (NotBoundException e) {

				e.printStackTrace();
			}
			
		} else if (event instanceof PingReceivedEvent) {
			// If leader, send a pulse, else send request not accepted.

			try {
				if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
						Status.LEADER.toString())) {
					LeaderMessageClient.sendPulse(((PingReceivedEvent) event)
							.getData());
				} else {
					LeaderMessageClient
							.sendRequestNotAccepted(((PingReceivedEvent) event)
									.getData());
				}
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
			} catch (RemoteException e) {
				
				e.printStackTrace();
			} catch (UnknownHostException e) {
			
				e.printStackTrace();
			} catch (NotBoundException e) {
			
				e.printStackTrace();
			}

		}else if(event instanceof PulseReceivedEvent){
			//Check the validity of receiving of this.
			if(EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(Status.NON_LEADER.toString())){
				//Authorized and valid. So reset the timer for pulse receive.
				timer.cancel();
				timer.schedule(new EpzillaPulseReceiveTimer(), 360000);
				
			}else{
				//TODO Invalid or unAutorized. Reject it.send unauth
				try {
					LeaderMessageClient.sendRequestNotAccepted(((PulseReceivedEvent)event).getData());
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
		}else if(event instanceof ListenerRegisteredEvent){
			//Send the pulse to the listener if the status is leader
			
				try {
					if(EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(Status.LEADER.toString())){
					LeaderMessageClient.sendPulse(((ListenerRegisteredEvent)event).getData());
					}else{
						LeaderMessageClient.sendRequestNotAccepted(((ListenerRegisteredEvent)event).getData());
					}
				} catch (MalformedURLException e) {
					
					e.printStackTrace();
				} catch (RemoteException e) {
					
					e.printStackTrace();
				} catch (UnknownHostException e) {
					
					e.printStackTrace();
				} catch (NotBoundException e) {
					
					e.printStackTrace();
				}
			
		}else if(event instanceof ErrorOccurredEvent){
			System.exit(-1);
		}
	}

}
