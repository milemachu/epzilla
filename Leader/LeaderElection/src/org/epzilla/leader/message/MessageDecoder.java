package org.epzilla.leader.message;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.epzilla.leader.Epzilla;
import org.epzilla.leader.client.DispatcherClientManager;
import org.epzilla.leader.client.NodeClientManager;
import org.epzilla.leader.event.ProcessStatusChangedEvent;
import org.epzilla.leader.event.PulseIntervalTimeoutEvent;
import org.epzilla.leader.event.PulseReceivedEvent;
import org.epzilla.leader.event.listner.EpZillaListener;
import org.epzilla.leader.util.Component;
import org.epzilla.leader.util.Status;

public class MessageDecoder {
	private EventHandler eventHandler;
	
	public MessageDecoder() {
		eventHandler=new EventHandler();
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
					
					//Start Threading for sending data
					final String nextHop=nextIp;
					Thread forwarder=new Thread(new Runnable() {
												
						public void run() {
							RmiMessageClient.forwardLeaderElectedMessage(nextHop, message);
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
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}else if(Integer.parseInt(strItems[0])==MessageMeta.UID){
			//LE has started
			Epzilla.setClusterLeader(null);
			Epzilla.setStatus(Status.UNKNOWN.name());
			Epzilla.setLeaderElectionRunning(true);
			eventHandler.fireEpzillaEvent(new ProcessStatusChangedEvent());
			//RUN LCR ALGO
		}
		
		return false;
	}
}
