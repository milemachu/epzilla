package org.epzilla.leader.message;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.epzilla.leader.event.PulseIntervalTimeoutEvent;

public class MessageDecoder {
	private EventHandler eventHandler;
	
	public MessageDecoder() {
		eventHandler=new EventHandler();
	}

	public boolean decodeMessage(String message){
		
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
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}else{}
		
		return false;
	}
}
