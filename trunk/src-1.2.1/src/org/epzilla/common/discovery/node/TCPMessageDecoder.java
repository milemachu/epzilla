package org.epzilla.common.discovery.node;

import org.epzilla.common.discovery.Constants;

public class TCPMessageDecoder implements Runnable {

	private String message;
	
	public TCPMessageDecoder(String message) {
		this.message=message;
	}
	
	public void run() {
		//0=message,1=ip
		String []tcpArr=message.split(Constants.TCP_UNICAST_DELIMITER);
		if(tcpArr[0].equalsIgnoreCase("SUBSCRIBE_LEADER_SERVICE") && NodeDiscoveryManager.isLeader()){
			
				NodeDiscoveryManager.getLeaderPublisher().addSubscription(tcpArr[1], tcpArr[0]);
			
		}else if(tcpArr[0].equalsIgnoreCase("UNSUBSCRIBE_LEADER_SERVICE") && NodeDiscoveryManager.isLeader()){
			NodeDiscoveryManager.getLeaderPublisher().removeSubscrition(tcpArr[1], tcpArr[0]);
		}
	}

}
