package org.epzilla.leader.message;

import java.util.concurrent.ConcurrentLinkedQueue;


public class RmiMessageHandler {
	private ConcurrentLinkedQueue<String> messageQueue;
	
	public RmiMessageHandler() {
		messageQueue=new ConcurrentLinkedQueue<String>();
	}
	
	public boolean enqueueMessage(String message){
		messageQueue.add(message);
		return true;
	}
	
	public boolean dequeueMessage(String message) {
		if(messageQueue.contains(message)){
			messageQueue.remove(message);
			return true;
		}
		return false;
	}


}
