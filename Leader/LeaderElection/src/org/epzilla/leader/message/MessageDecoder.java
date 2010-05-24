package org.epzilla.leader.message;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.epzilla.leader.Epzilla;
import org.epzilla.leader.EpzillaLeaderPubSub;
import org.epzilla.leader.LCRAlgoImpl;
import org.epzilla.leader.client.DispatcherClientManager;
import org.epzilla.leader.client.NodeClientManager;
import org.epzilla.leader.event.PingReceivedEvent;
import org.epzilla.leader.event.ProcessStatusChangedEvent;
import org.epzilla.leader.event.PulseIntervalTimeoutEvent;
import org.epzilla.leader.event.PulseReceivedEvent;
import org.epzilla.leader.event.RequestRejectedEvent;
import org.epzilla.leader.event.listner.EpZillaListener;
import org.epzilla.leader.util.Component;
import org.epzilla.leader.util.Status;

public class MessageDecoder {
	private EventHandler eventHandler;
	private LCRAlgoImpl lcrAlgorithm;
	
	public MessageDecoder() {
		eventHandler=new EventHandler();
		lcrAlgorithm=new LCRAlgoImpl();
	}

	public boolean decodeMessage(final String message){
		
		//0=MessageCode
		String[] strItems = message.split(Character.toString(MessageMeta.SEPARATOR));
		String messageType=MessageGenerator.getMessage(Integer.parseInt(strItems[0]));
		System.out.println("Decoding the received message "+messageType);
		
		//Starting the decoding process
		if (Integer.parseInt(strItems[0]) == MessageMeta.LEADER) {
			//LEADER message to inform about the new Leader
			try {
				if (strItems[1].equalsIgnoreCase(InetAddress.getLocalHost()
						.getHostAddress())) {
					//This is the leader
					System.out.println("Localhost is the Leader");
					eventHandler.fireEpzillaEvent(new PulseIntervalTimeoutEvent());
					return true;
				}else{
					//This is not the leader
					Epzilla.setClusterLeader(strItems[1]);
					Epzilla.setStatus(Status.NON_LEADER.name());
					Epzilla.setLeaderElectionRunning(false);
					eventHandler.fireEpzillaEvent(new ProcessStatusChangedEvent());
					if(Epzilla.getComponentType().equalsIgnoreCase(Component.NODE.name())){
						NodeClientManager.setClusterLeader(strItems[1]);
					}
										
					//Start Threading for sending data
					Thread forwarder=new Thread(new Runnable() {												
						public void run() {
							RmiMessageClient.forwardLeaderElectedMessage(getNextHopToCommunicate(), message);
						}
					});					
					forwarder.start();
					
					Thread executor=new Thread(new Runnable() {												
						public void run() {
							RmiMessageClient.registerListenerWithLeader(Epzilla.getClusterLeader(), new EpZillaListener());
						}
					});					
					executor.start();
					
					eventHandler.fireEpzillaEvent(new PulseReceivedEvent(strItems[1]));
					return true;
				}
			} catch (UnknownHostException e) {
				System.out.println("Error occured.");
				return false;
			}
		}else if(Integer.parseInt(strItems[0])==MessageMeta.UID){
			//LE has started
			Epzilla.setClusterLeader(null);
			Epzilla.setStatus(Status.UNKNOWN.name());
			Epzilla.setLeaderElectionRunning(true);
			eventHandler.fireEpzillaEvent(new ProcessStatusChangedEvent());
			if(Epzilla.getComponentType().equalsIgnoreCase(Component.NODE.name())){
				NodeClientManager.setClusterLeader(null);
			}
			//RUN LCR ALGO
			String result=lcrAlgorithm.runAlgorithm(message);
			//Only 3 outcomes. 1=LEADER, if UID is same. 2=NON_LEADER, if received UID is small. 3=UNKNOWN, if received UID is large.
			if(result.equalsIgnoreCase(Status.LEADER.name())){
				//Received UID is this node's UID
				Epzilla.setClusterLeader(strItems[1]);
				Epzilla.setStatus(Status.LEADER.name());
				Epzilla.setLeaderElectionRunning(false);
				eventHandler.fireEpzillaEvent(new ProcessStatusChangedEvent());
				if(Epzilla.getComponentType().equalsIgnoreCase(Component.NODE.name())){
					NodeClientManager.setClusterLeader(strItems[1]);
				}
				EpzillaLeaderPubSub.initializePubSub();
				//Starting Sender Thread
				Thread sender=new Thread(new Runnable() {
					public void run() {
						RmiMessageClient.sendLeaderElectedMessage(getNextHopToCommunicate());
					}
				});
				sender.start();
			}else if(result.equalsIgnoreCase(Status.NON_LEADER.name())){
				//Received UID is smaller than this UID.
				Thread forwarder=new Thread(new Runnable() {
					public void run() {
						RmiMessageClient.forwardReceivedUidMessage(getNextHopToCommunicate(), message);
					}
				});
				forwarder.start();
			}else{
				//Received UID is larger than this UID.
				Thread sender=new Thread(new Runnable() {
					public void run() {
						RmiMessageClient.sendUidMessage(getNextHopToCommunicate());
					}
				});
				sender.start();
			}
		}else if(Integer.parseInt(strItems[0])==MessageMeta.PULSE){
			System.out.println("Pulse received.");
			eventHandler.fireEpzillaEvent(new PulseReceivedEvent(strItems[1]));
		}else if(Integer.parseInt(strItems[0])==MessageMeta.PING_LEADER){
			System.out.println("Ping received.");
			eventHandler.fireEpzillaEvent(new PingReceivedEvent(strItems[1]));
		}else if(Integer.parseInt(strItems[0])==MessageMeta.REQUEST_NOT_ACCEPTED){
			System.out.println("Request Not Accepted received.");
			eventHandler.fireEpzillaEvent(new RequestRejectedEvent(Integer.parseInt(strItems[2])));
		}
		
		return true;
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
