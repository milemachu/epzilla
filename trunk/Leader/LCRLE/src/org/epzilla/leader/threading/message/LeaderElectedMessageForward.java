package org.epzilla.leader.threading.message;

import org.epzilla.leader.rmi.LeaderMessageClient;

public class LeaderElectedMessageForward implements Runnable {
	String receiver;
	String messageToBeSent;

	public LeaderElectedMessageForward(String receiver,String messagetoBeSent) {
		this.receiver=receiver;
		this.messageToBeSent=messagetoBeSent;
	}
	
	public void run() {
		try {
			LeaderMessageClient.forwardLeaderElectedMessage(receiver, messageToBeSent);
		} catch (Exception e) {
			System.out.println("Leader Elected Message forwarding to "+receiver+ "failed.");
		}

	}

}
