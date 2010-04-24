package org.epzilla.common.discovery.dispatcher;

import java.util.Hashtable;

import org.epzilla.common.discovery.Constants;
import org.epzilla.common.discovery.IServicePublisher;
import org.epzilla.common.discovery.multicast.*;

public class DispatcherPublisher implements IServicePublisher {
	private String serviceName="DISPATCHER_SERVICE";	
	private String multicastGroupIp="224.0.0.2";
	private int multicastPort=5005;
	private Hashtable<Integer, String> clusterLeaderIp=new Hashtable<Integer, String>();
	
	public DispatcherPublisher() {
	}

	public boolean addSubscription(String serviceClient, String serviceName) {
		if(serviceName.equalsIgnoreCase(this.serviceName)){
			synchronized (clusterLeaderIp) {
				String  []arr=serviceClient.split(Constants.DISPATCHER_CLIENT_DELIMITER);
				clusterLeaderIp.put(Integer.parseInt(arr[0]), arr[1]);
				return true;
			}
		}
			return false;

	}

	public boolean publishService() {
		MulticastSender broadcaster=new MulticastSender(multicastGroupIp,multicastPort);
		broadcaster.broadcastMessage(serviceName);
		return true;
	}

	public boolean removeSubscrition(String serviceClient, String serviceName) {
		if(serviceName.equalsIgnoreCase(this.serviceName)){
			synchronized (clusterLeaderIp){
				clusterLeaderIp.remove(Integer.parseInt(serviceClient.split(Constants.DISPATCHER_CLIENT_DELIMITER)[0]));
				return true;
			}
		}
		
		return false;
	}

}
