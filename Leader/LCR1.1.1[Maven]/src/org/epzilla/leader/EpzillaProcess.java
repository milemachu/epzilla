package org.epzilla.leader;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.epzilla.leader.event.RequestRejectedEvent;
import org.epzilla.leader.message.RejectReason;
import org.epzilla.leader.rmi.LeaderMessageClient;
import org.epzilla.leader.util.Status;

public class EpzillaProcess {

	// This is the UID of this host
	private byte UID = 1;
	private String status;
	private boolean isDefaultLeader;
	private boolean isLeaderElectionRunning;
	private InetAddress defaultClusterLeader;
	private InetAddress clusterLeader;
	private Vector<InetAddress> clusterIpList;
	private Vector<InetAddress> clusterAliveIpList;
	private HashMap<Integer, String> clsuterIpWithUid;
	private Timer timer;



	// Private constructor prevents instantiation from other classes
	private EpzillaProcess() {
		// change these according to the node.Done by config loader.
		setStatus(Status.UNKNOWN.toString());
		setDefaultLeader(false);
		setLeaderElectionRunning(false);
		setClusterLeader(null);
		// Add these from some where
		setClusterIpList(new Vector<InetAddress>());
		// getClusterIpList().add(e)
		setClsuterIpWithUid(new HashMap<Integer, String>());
		// Get these from the STM and update later. initially none
		setClusterAliveIpList(new Vector<InetAddress>());
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	public byte getUID() {
		return UID;
	}

	public void setUID(byte uID) {
		UID = uID;
	}
	
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

	public synchronized InetAddress getDefaultClusterLeader() {
		return defaultClusterLeader;
	}

	public synchronized void setDefaultClusterLeader(
			InetAddress defaultClusterLeader) {
		this.defaultClusterLeader = defaultClusterLeader;
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

	public HashMap<Integer, String> getClsuterIpWithUid() {
		return clsuterIpWithUid;
	}

	public void setClsuterIpWithUid(HashMap<Integer, String> clsuterIpWithUid) {
		this.clsuterIpWithUid = clsuterIpWithUid;
	}

	public synchronized void setClusterAliveIpList(
			Vector<InetAddress> clusterAliveIpList) {
		this.clusterAliveIpList = clusterAliveIpList;
	}

	public synchronized Vector<InetAddress> getClusterAliveIpList() {
		return clusterAliveIpList;
	}

	public synchronized boolean isLeaderElectionRunning() {
		return isLeaderElectionRunning;
	}

	public synchronized void setLeaderElectionRunning(
			boolean isLeaderElectionRunning) {
		this.isLeaderElectionRunning = isLeaderElectionRunning;
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

	public String getIpNextToThis() {
		EpzillaProcess ep = EpzillaProcess.getInstance();

		String nextIp = ep.getClsuterIpWithUid().get(
				new Integer(EpzillaProcess.getInstance().getUID() + 1));
		// null if this is the last host
		if (nextIp == null) {
			nextIp = new ArrayList<String>(ep.getClsuterIpWithUid().values())
					.get(0);
		}

		return nextIp;
	}

	public synchronized void fireEpzillaEvent(IEpzillaEvent event) {
		timer = new Timer();

		if (event instanceof ProcessStatusChangedEvent) {
			// Check whether the new status is nonLeader or Unknown
			// If above true, reset timer and epZilla leader
			if (!EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
					Status.LEADER.toString())) {
				EpzillaLeader.getInstance().setClusterClientList(null);
			}
			timer.cancel();
			timer = new Timer();
			System.out
					.println("Epzilla Process Status changed Event Fired. All the scheduled tasks are removed.");
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

				// RESET the Timer for Pulse
				timer.cancel();
				timer = new Timer();
				timer.schedule(new EpzillaPulseSendTimer(), 240000);
				System.out.println("Pulse sending time out event fired.");
			}
		} else if (event instanceof PulseNotReceivedTimeoutEvent) {
			// Client has not received the pulse after 360 default interval.

			try {
				if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
						Status.NON_LEADER.toString())) {
					/**
					 * New Approach used //Has to be the ip below/above this
					 * host String
					 * leader=LeaderMessageClient.getClusterLeaderFromRemote
					 * ("IP Next to me");
					 * if(EpzillaProcess.getInstance().getClusterLeader
					 * ().getHostAddress().equalsIgnoreCase(leader)){ //Same
					 * leader. Just send a PING
					 * LeaderMessageClient.sendPing(EpzillaProcess.getInstance()
					 * .getClusterLeader().getHostAddress()); }else{ //Different
					 * Leader. Still need to send a pulse. //When pulse
					 * received, normal timers will be back.
					 * EpzillaProcess.getInstance
					 * ().setClusterLeader(InetAddress.getByName(leader));
					 * LeaderMessageClient.registerListenerWithLeader(leader,
					 * new EpZillaListener()); }
					 **/

					String serverState = LeaderMessageClient
							.getStateFromRemote(EpzillaProcess.getInstance()
									.getClusterLeader().getHostAddress());
					String serverClusterLeader = LeaderMessageClient
							.getClusterLeaderFromRemote(EpzillaProcess
									.getInstance().getClusterLeader()
									.getHostAddress());

					if (serverState.equalsIgnoreCase(Status.LEADER.toString())) {
						// just send the listener.
						LeaderMessageClient.registerListenerWithLeader(
								serverClusterLeader, new EpZillaListener());
						// SEnd the PING now. -Redundent check.
						LeaderMessageClient.sendPing(serverClusterLeader);
						// Now I shud deffa get a PULSE. If not this is an
						// error.So wait for it.
					} else if (serverState.equalsIgnoreCase(Status.UNKNOWN
							.toString())
							&& serverClusterLeader == null) {
						// Leader election running
						// Wait and see
					}

					// In both cases, they expect a pulse. So have to re-init
					// the timer for that.

					// timer.cancel();
					// timer.schedule(new EpzillaPulseReceiveTimer(), 360000);

				}
				// Wait 10 mins and exit from the system
				// They have waited at lease once. They are trying to start it
				// over.
				// If this time response received, every thing will be back to
				// normal.
				timer.cancel();
				timer = new Timer();
				timer.schedule(new EpzillaErrorStateExitTimer(), 600000);

			} catch (Exception e) {
				System.out
						.println("Server host unavailable at this time. Server is down and initiate a new Leader election.");
				System.out.println(e.getMessage());

				if (e instanceof ConnectException
						|| e instanceof java.rmi.ConnectException
						|| e instanceof UnknownHostException) {
					// This means server is down.
					// Change the status and leader of the host.
					// Initiate Leader Election by sending UID to next host.
					EpzillaProcess ep = EpzillaProcess.getInstance();

					ep.setClusterLeader(null);
					ep.setStatus(Status.UNKNOWN.toString());
					ep.setLeaderElectionRunning(true);
					ep.fireEpzillaEvent(new ProcessStatusChangedEvent());

					try {
						LeaderMessageClient
								.sendUidMessage(ep.getIpNextToThis());
					} catch (MalformedURLException e1) {

						e1.printStackTrace();
					} catch (RemoteException e1) {

						e1.printStackTrace();
					} catch (UnknownHostException e1) {

						e1.printStackTrace();
					} catch (NotBoundException e1) {

						e1.printStackTrace();
					}
				}

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
							.sendRequestNotAccepted(
									((PingReceivedEvent) event).getData(),
									RejectReason.NOT_ALLOWED_TO_RECEIVE_PING_NOT_LEADER);
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

		} else if (event instanceof PulseReceivedEvent) {
			// Check the validity of receiving of this.
			if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
					Status.NON_LEADER.toString())) {
				// Authorized and valid. So reset the timer for pulse receive.
				timer.cancel();
				timer = new Timer();
				timer.schedule(new EpzillaPulseReceiveTimer(), 360000);

			} else {
				// Invalid or unAutorized. Reject it.send unauth
				try {
					LeaderMessageClient
							.sendRequestNotAccepted(
									((PulseReceivedEvent) event).getData(),
									RejectReason.NOT_ALLOWED_TO_RECEIVE_PULSE_NOT_NON_LEADER);
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
		} else if (event instanceof ListenerRegisteredEvent) {
			// Send the pulse to the listener if the status is leader

			try {
				if (EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
						Status.LEADER.toString())) {
					LeaderMessageClient
							.sendPulse(((ListenerRegisteredEvent) event)
									.getData());
				} else {
					LeaderMessageClient
							.sendRequestNotAccepted(
									((ListenerRegisteredEvent) event).getData(),
									RejectReason.NOT_ALLOWED_TO_REGISTER_LISTNER_NOT_LEADER);
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

		} else if (event instanceof ErrorOccurredEvent) {
			System.out.println("Error Occured. Exiting the system.");
			System.exit(-1);
		} else if (event instanceof RequestRejectedEvent) {
			int errorCode = ((RequestRejectedEvent) event).getData();

			switch (errorCode) {
			case 400:
				if (!EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
						Status.LEADER.toString())) {
					timer.cancel();
					timer = new Timer();
					timer.schedule(new EpzillaErrorStateExitTimer(), 12000);
				}
				break;
			case 401:
				if (!EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
						Status.NON_LEADER.toString())) {
					timer.cancel();
					timer = new Timer();
					timer.schedule(new EpzillaErrorStateExitTimer(), 100);
				}
				break;

			case 402:
				if (!EpzillaProcess.getInstance().getStatus().equalsIgnoreCase(
						Status.LEADER.toString())) {
					timer.cancel();
					timer = new Timer();
					timer.schedule(new EpzillaErrorStateExitTimer(), 100);
				}

			default:
				break;
			}
		}
	}

}
