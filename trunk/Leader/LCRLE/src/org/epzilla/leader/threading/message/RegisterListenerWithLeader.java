package org.epzilla.leader.threading.message;

import org.epzilla.leader.event.IEpzillaEventListner;
import org.epzilla.leader.rmi.LeaderMessageClient;

public class RegisterListenerWithLeader implements Runnable {
	String receiver;
	IEpzillaEventListner listener;
	
	public RegisterListenerWithLeader(String receiver,IEpzillaEventListner listener) {
		this.receiver=receiver;
		this.listener=listener;
	}
	
	
	public void run() {
		try {
			LeaderMessageClient.registerListenerWithLeader(receiver, listener);
		} catch (Exception e) {
			System.out.println("Registering listener with the Leader "+receiver+" is not successful.");
		}
	}

}
