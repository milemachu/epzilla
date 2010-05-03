package org.epzilla.common.discovery.dispatcher;

import org.epzilla.common.discovery.Constants;

public class MulticastMessageDecoder implements Runnable {
//Handle the incoming broadcast messages from other dispatchers to elect the leader.
	private String message;
	
	public MulticastMessageDecoder(String message) {
		this.message=message;
	}
	@Override
	public void run() {
		//0=message;1=sender
		String []mcArr=message.split(Constants.MULTICAST_DELIMITER);
		if(mcArr[0].equalsIgnoreCase("DISPATCHER_SERVICE") /*&& !DispatcherDiscoveryManager.getDispatcherPublisher().getDispatchers().contains(mcArr[1])*/){
			DispatcherDiscoveryManager.getDispatcherPublisher().insertDispatcher(mcArr[1]);
		}
	}

}
