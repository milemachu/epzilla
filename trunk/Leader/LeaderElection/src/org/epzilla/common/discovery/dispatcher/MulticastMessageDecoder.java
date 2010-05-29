package org.epzilla.common.discovery.dispatcher;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.unicast.TCPSender;

public class MulticastMessageDecoder implements Runnable {
//Handle the incoming broadcast messages from other dispatchers to elect the leader.
	private String message;
	private int tcpPort=5010;
	
	public MulticastMessageDecoder(String message) {
		this.message=message;
	}
	@Override
	public void run() {
		//0=message;1=sender
		String []mcArr=message.split(Constants.MULTICAST_DELIMITER);
		if(mcArr[0].equalsIgnoreCase("DISPATCHER_SERVICE") && !DispatcherDiscoveryManager.getDispatcherPublisher().getDispatchers().contains(mcArr[1])){
			DispatcherDiscoveryManager.getDispatcherPublisher().insertDispatcher(mcArr[1]);
		}else if(mcArr[0].equalsIgnoreCase("DISPATCHER_LEADER_SERVICE") && DispatcherDiscoveryManager.getDispatcherLeader()==null){
			DispatcherDiscoveryManager.setDispatcherLeader(mcArr[1]);
			//send a tcp msg to subscribe with it.
			TCPSender ts=new TCPSender(mcArr[1], tcpPort);
			ts.sendMessage("SUBSCRIBE_DISPATCHER_LEADER_SERVICE");
		}
	}
}
