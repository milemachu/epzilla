package org.epzilla.common.discovery.node;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.unicast.TCPSender;

public class MulticastMessageDecoder implements Runnable {

	private String message;
	
	public MulticastMessageDecoder(String message) {
	this.message=message;
	}
	
	public void run() {
		//0=message;1=multicastSender
		String []mcArr=message.split(Constants.MULTICAST_DELIMITER);
		if(mcArr[0].equalsIgnoreCase("DISPATCHER_SERVICE")){
			//If this is the leader subscribe. else forget it.
			//Send TCP connections to them.
			if(NodeDiscoveryManager.isLeader()){
				TCPSender ts=new TCPSender("127.0.0.1", 5010);
				ts.sendMessage(NodeDiscoveryManager.getClusterId()+Constants.DISPATCHER_CLIENT_DELIMITER+"SUBSCRIBE_DISPATCHER_SERVICE");
				
				//Now update the dispatcher list in the leader service.
				((LeaderPublisher)NodeDiscoveryManager.getPublisher()).updateDispatcherList(mcArr[1]);
			}
		}else if(mcArr[0].equalsIgnoreCase("LEADER_SERVICE")){
			//if this is a node client subscribe it.else forget it.
			//Send the TCP connection.
			if(!NodeDiscoveryManager.isLeader()){
				//Wait till the Leader publisher imple finishes.
			}
		}else if(mcArr[0].equalsIgnoreCase("NODE_SERVICE")){
			//message from another node. Add this to our node list.
		}
	}

}
