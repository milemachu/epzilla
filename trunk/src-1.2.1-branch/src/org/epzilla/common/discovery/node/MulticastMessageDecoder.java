package org.epzilla.common.discovery.node;

import java.util.HashSet;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.unicast.TCPSender;
import org.epzilla.leader.Epzilla;

/**
 * This is the multicast message decoder of the cluster nodes and the received multicast messages are sent here and necessary actions are taken from here.
 * @author Administrator
 *
 */
public class MulticastMessageDecoder implements Runnable {

	private String message;
	private int tcpPort=5010;
    private static String LEADER_SERVICE_NAME= "LEADER_SERVICE";
    private static String DISPATCHER_SERVICE_NAME= "DISPATCHER_SERVICE";
    private static String NODE_SERVICE_NAME= "NODE_SERVICE";
    private static String SUBSCRIBE_PREFIX = "SUBSCRIBE_";
	@SuppressWarnings("unused")
	private static String UNSUBSCRIBE_PREFIX = "UNSUBSCRIBE_";
	private HashSet<String> authorizedNodeList=new HashSet<String>(Epzilla.getComponentIpList().values());
	
	public MulticastMessageDecoder(String message) {
	this.message=message;
	}
	
	public void run() {
		//0=message;1=multicastSender
		String []mcArr=message.split(Constants.MULTICAST_DELIMITER);
		if(mcArr[0].equalsIgnoreCase(DISPATCHER_SERVICE_NAME)){
			//If this is the leader subscribe. else forget it.
			//Send TCP connections to them.
			if(NodeDiscoveryManager.isLeader() && !NodeDiscoveryManager.getLeaderPublisher().getDispatchers().contains(mcArr[1])){
				TCPSender ts=new TCPSender(mcArr[1], 5010);
				ts.sendMessage(NodeDiscoveryManager.getClusterId()+Constants.DISPATCHER_CLIENT_DELIMITER+SUBSCRIBE_PREFIX+DISPATCHER_SERVICE_NAME);
				
				//Now update the dispatcher list in the leader service.
				NodeDiscoveryManager.getLeaderPublisher().updateDispatcherList(mcArr[1]);
			}
			//Checking auth list for security.
		}else if(mcArr[0].startsWith(LEADER_SERVICE_NAME)  && authorizedNodeList.contains(mcArr[1])){
			//if this is a node client subscribe it.else forget it.
			//Send the TCP connection.
			if(!NodeDiscoveryManager.isLeader()){
				//Wait till the Leader publisher imple finishes.
				//0-LEADER_SERVICE;1-ClusterId
				String []info=mcArr[0].split(Constants.CLUSTER_ID_DELIMITER);
				if(Integer.parseInt(info[1])==NodeDiscoveryManager.getClusterId() && !NodeDiscoveryManager.isSubscribedWithLeader()){
					NodeDiscoveryManager.setClusterLeader(mcArr[1]);
					NodeDiscoveryManager.setSubscribedWithLeader(true);
					
					//send a tcp msg to subscribe with it.
					TCPSender ts=new TCPSender(mcArr[1], tcpPort);
					ts.sendMessage(SUBSCRIBE_PREFIX+LEADER_SERVICE_NAME);
				}
			}
			//Checking auth list for security.
		}else if(mcArr[0].startsWith(NODE_SERVICE_NAME)  && authorizedNodeList.contains(mcArr[1])){
			//message from another node. Add this to our node list.
			//0-NODE_SERVICE;1-ClusterId
			String []info=mcArr[0].split(Constants.CLUSTER_ID_DELIMITER);
			if(Integer.parseInt(info[1])==NodeDiscoveryManager.getClusterId() && !NodeDiscoveryManager.getNodePublisher().getNodes().contains(mcArr[1])){
							//No msg. Just add update this side
				NodeDiscoveryManager.getNodePublisher().addSubscription(mcArr[1], SUBSCRIBE_PREFIX+NODE_SERVICE_NAME);
			
			}
		}
	}

}
