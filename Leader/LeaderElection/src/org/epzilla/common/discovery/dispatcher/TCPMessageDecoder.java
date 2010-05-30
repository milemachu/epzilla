package org.epzilla.common.discovery.dispatcher;

import org.epzilla.common.discovery.Constants;

public class TCPMessageDecoder implements Runnable {

	private String message;
	
	public TCPMessageDecoder(String message) {
		this.message=message;
	}
	
	@Override
	public void run() {
		//0=message,1=ip
		String []tcpArr=message.split(Constants.TCP_UNICAST_DELIMITER);
		if(tcpArr[0].equalsIgnoreCase("SUBSCRIBE_DISPATCHER_LEADER_SERVICE") && DispatcherDiscoveryManager.isLeader()){
			DispatcherDiscoveryManager.getLeaderPublisher().addSubscription(tcpArr[1], tcpArr[0]);
		}else if(tcpArr[0].equalsIgnoreCase("UNSUBSCRIBE_DISPATCHER_LEADER_SERVICE") && DispatcherDiscoveryManager.isLeader()){
			DispatcherDiscoveryManager.getLeaderPublisher().removeSubscrition(tcpArr[1], tcpArr[0]);
		}else{
			//0=cluster id,1=service name
			String []arr=tcpArr[0].split(Constants.DISPATCHER_CLIENT_DELIMITER);
			if(arr[1].equalsIgnoreCase("SUBSCRIBE_DISPATCHER_SERVICE")){
				DispatcherDiscoveryManager.getDispatcherPublisher().addSubscription(arr[0]+Constants.DISPATCHER_CLIENT_DELIMITER+tcpArr[1], arr[1]);
			}else if(arr[1].equalsIgnoreCase("UNSUBSCRIBE_DISPATCHER_SERVICE")){
				DispatcherDiscoveryManager.getDispatcherPublisher().removeSubscrition(arr[0]+Constants.DISPATCHER_CLIENT_DELIMITER+tcpArr[1], arr[1]);
			}
		}
	}

}
