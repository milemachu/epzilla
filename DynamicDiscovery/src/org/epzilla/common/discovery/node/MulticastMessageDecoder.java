package org.epzilla.common.discovery.node;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.unicast.TCPSender;

public class MulticastMessageDecoder implements Runnable {

	private String message;
	private int tcpPort=5010;
	
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
				TCPSender ts=new TCPSender(mcArr[1], 5010);
				ts.sendMessage(NodeDiscoveryManager.getClusterId()+Constants.DISPATCHER_CLIENT_DELIMITER+"SUBSCRIBE_DISPATCHER_SERVICE");
				
				//Now update the dispatcher list in the leader service.
				((LeaderPublisher)NodeDiscoveryManager.getPublisher()).updateDispatcherList(mcArr[1]);
			}
		}else if(mcArr[0].startsWith("LEADER_SERVICE")){
			//if this is a node client subscribe it.else forget it.
			//Send the TCP connection.
			if(!NodeDiscoveryManager.isLeader()){
				//Wait till the Leader publisher imple finishes.
				//0-LEADER_SERVICE;1-ClusterId
				String []info=mcArr[0].split(Constants.CLUSTER_ID_DELIMITER);
				if(Integer.parseInt(info[1])==NodeDiscoveryManager.getClusterId()){
					NodeDiscoveryManager.setClusterLeader(mcArr[1]);
					//send a tcp msg to subscribe with it.
					TCPSender ts=new TCPSender(mcArr[1], tcpPort);
					ts.sendMessage("SUBSCRIBE_LEADER_SERVICE");
				}
			}
		}else if(mcArr[0].startsWith("NODE_SERVICE")){
			//message from another node. Add this to our node list.
			//0-NODE_SERVICE;1-ClusterId
			String []info=mcArr[0].split(Constants.CLUSTER_ID_DELIMITER);
			if(Integer.parseInt(info[1])==NodeDiscoveryManager.getClusterId() && !((NodePublisher)NodeDiscoveryManager.getPublisher()).getNodes().contains(mcArr[1])){
							//No msg. Just add update this side
				((NodePublisher)NodeDiscoveryManager.getPublisher()).addSubscription(mcArr[1], "SUBSCRIBE_NODE_SERVICE");
			
			}
		}
	}

}
