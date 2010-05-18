package org.epzilla.leader.message;

import java.util.Iterator;

import org.epzilla.leader.Epzilla;
import org.epzilla.leader.EpzillaLeaderPubSub;
import org.epzilla.leader.client.DispatcherClientManager;
import org.epzilla.leader.client.NodeClientManager;
import org.epzilla.leader.event.ErrorOccurredEvent;
import org.epzilla.leader.event.IEpzillaEvent;
import org.epzilla.leader.event.PingReceivedEvent;
import org.epzilla.leader.event.ProcessStatusChangedEvent;
import org.epzilla.leader.event.PulseIntervalTimeoutEvent;
import org.epzilla.leader.event.PulseNotReceivedTimeoutEvent;
import org.epzilla.leader.event.PulseReceivedEvent;
import org.epzilla.leader.event.RequestRejectedEvent;
import org.epzilla.leader.event.listner.EpZillaListener;
import org.epzilla.leader.event.listner.IEpzillaEventListner;
import org.epzilla.leader.util.Component;
import org.epzilla.leader.util.Status;

public class EventHandler {
	
	public boolean fireEpzillaEvent(final IEpzillaEvent event){
		if(event instanceof ProcessStatusChangedEvent){
			System.out.println("Process status changed to:"+Epzilla.getStatus());
			Epzilla.resetTimerQueue();
			//Added later
			EpzillaLeaderPubSub.resetPubSub();
		}else if(event instanceof PulseIntervalTimeoutEvent){
			if(Epzilla.getComponentType().equalsIgnoreCase(Component.NODE.name())){
				if(Epzilla.getStatus().equalsIgnoreCase(Status.LEADER.name())){
					Iterator<IEpzillaEventListner> iterator=EpzillaLeaderPubSub.getClientListenerList().iterator();
					while (iterator.hasNext()) {
						final IEpzillaEventListner iEpzillaEventListner = (IEpzillaEventListner) iterator
								.next();
						Thread sender=new Thread(new Runnable() {
							public void run() {
								RmiMessageClient.sendPulse(iEpzillaEventListner.getData());
							}
						});
						sender.start();
					}
					Epzilla.resetTimerQueue();
					Epzilla.addToTimerQueue(new PulseIntervalTimeoutEvent());
				}
			}
		}else if(event instanceof PulseNotReceivedTimeoutEvent){
			if(Epzilla.getStatus().equalsIgnoreCase(Status.NON_LEADER.name())){
				String serverStatus=RmiMessageClient.getStateFromRemote(Epzilla.getClusterLeader());
				if(serverStatus!=null){
					if(serverStatus.equalsIgnoreCase(Status.LEADER.name())){
						Thread committer=new Thread(new Runnable() {
							public void run() {
								RmiMessageClient.registerListenerWithLeader(Epzilla.getClusterLeader(), new EpZillaListener());
								RmiMessageClient.sendPing(Epzilla.getClusterLeader());
							}
						});
						committer.start();
					}else if(serverStatus.equalsIgnoreCase(Status.UNKNOWN.name())){
						boolean isLERunning=RmiMessageClient.isLeaderElectioRunningInRemote(Epzilla.getClusterLeader());
						if(isLERunning){}
					}
					Epzilla.resetTimerQueue();
					Epzilla.addToTimerQueue(new ErrorOccurredEvent());
				}else{
					//Server not available. Initiate LE.
					Epzilla.setClusterLeader(null);
					Epzilla.setStatus(Status.UNKNOWN.name());
					Epzilla.setLeaderElectionRunning(true);
					fireEpzillaEvent(new ProcessStatusChangedEvent());					
					Thread initiator=new Thread(new Runnable() {
						public void run() {
							RmiMessageClient.sendUidMessage(getNextHopToCommunicate());
						}
					});
					initiator.start();
				}
			}
		}else if(event instanceof PingReceivedEvent){
			if (Epzilla.getStatus().equalsIgnoreCase(Status.LEADER.name())) {
				Thread informer=new Thread(new Runnable() {
					public void run() {
						RmiMessageClient.sendPulse(((PingReceivedEvent) event)
						.getData());
					}
				});
				informer.start();
			} else {
				Thread sender=new Thread(new Runnable() {
					public void run() {
						RmiMessageClient.sendRequestNotAccepted(((PingReceivedEvent) event).getData(), RejectReason.NOT_ALLOWED_TO_RECEIVE_PING_NOT_LEADER);
					}
				});
				sender.start();
				
			}
		}else if(event instanceof PulseReceivedEvent){
			if(Epzilla.getStatus().equalsIgnoreCase(Status.NON_LEADER.name())){
				Epzilla.resetTimerQueue();
				Epzilla.addToTimerQueue(new PulseNotReceivedTimeoutEvent());
			}else{
				Thread informer=new Thread(new Runnable() {
					public void run() {
						RmiMessageClient.sendRequestNotAccepted(((PulseReceivedEvent) event).getData(), RejectReason.NOT_ALLOWED_TO_RECEIVE_PULSE_NOT_NON_LEADER);
					}
				});
				informer.start();
			}
		}else if(event instanceof ErrorOccurredEvent){
			System.out.println("Error occured in the system.");
		}else if(event instanceof RequestRejectedEvent){
			int errorCode = ((RequestRejectedEvent) event).getData();
			boolean isErrorOccured=false;
			switch (errorCode) {
			case 400:
				if(!Epzilla.getStatus().equalsIgnoreCase(Status.LEADER.name()))
					isErrorOccured=true;
				break;
			case 401:
				if(!Epzilla.getStatus().equalsIgnoreCase(Status.NON_LEADER.name()))
					isErrorOccured=true;
				break;
			case 402:
				if(!Epzilla.getStatus().equalsIgnoreCase(Status.LEADER.name()))
					isErrorOccured=true;
				break;
			default:
				break;
			}			
			if(isErrorOccured)
				Epzilla.addToTimerQueue(new ErrorOccurredEvent());
			
		}		
		return false;
	}
	
	private String getNextHopToCommunicate(){
		String componentType=Epzilla.getComponentType();
		String nextIp = null;
		if(componentType.equalsIgnoreCase(Component.DISPATCHER.name())){
			//get next dispatcher
			nextIp=DispatcherClientManager.getNextDispatcher();
		}else if(componentType.equalsIgnoreCase(Component.NODE.name())){
			//get next node
			nextIp=NodeClientManager.getNextNode();
		}else{
			//get next wahtever
		}		
		return nextIp;
	}
}
